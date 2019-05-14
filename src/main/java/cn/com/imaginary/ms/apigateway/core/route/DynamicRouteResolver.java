package cn.com.imaginary.ms.apigateway.core.route;

import cn.com.imaginary.ms.apigateway.model.ReturnData;
import cn.com.imaginary.ms.apigateway.util.ReturnDataUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.*;
import org.springframework.cloud.gateway.support.NotFoundException;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.com.imaginary.ms.apigateway.common.constants.StringConsts.GATEWAY_ROUTES;
import static cn.com.imaginary.ms.apigateway.common.enums.UrlProtocal.LB;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/12 11:25
 */
@Slf4j
@Service
public class DynamicRouteResolver implements ApplicationEventPublisherAware {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;

    @Autowired
    private RouteDefinitionLocator routeDefinitionLocator;
    @Autowired
    private RouteLocator routeLocator;

    public ReturnData add(RouteDefinition inputDefinition) {
        RouteDefinition definition = assembleRouteDefinition(inputDefinition);
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        redisTemplate.opsForHash().put(GATEWAY_ROUTES, definition.getId(),
                JSONObject.toJSONString(definition));
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return ReturnDataUtil.getSuccussReturn();
    }

    public ReturnData update(RouteDefinition inputDefinition) {
        RouteDefinition definition = assembleRouteDefinition(inputDefinition);
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            log.info("update error in delete old", e);
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            redisTemplate.opsForHash().put(GATEWAY_ROUTES, definition.getId(),
                    JSONObject.toJSONString(definition));
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return ReturnDataUtil.getSuccussReturn();
        } catch (Exception e) {
            return ReturnDataUtil.systemError();
        }
    }

    public Mono<ResponseEntity<Object>> delete(String id) {
        redisTemplate.opsForHash().delete(GATEWAY_ROUTES, id);
        return this.routeDefinitionWriter.delete(Mono.just(id))
                .then(Mono.defer(() -> Mono.just(ResponseEntity.ok().build())))
                .onErrorResume(t -> t instanceof NotFoundException, t -> Mono.just(ResponseEntity.notFound().build()));
    }

    public Mono<List<Map<String, Object>>> getRoutesList() {
        Mono<Map<String, RouteDefinition>> routeDefs = routeDefinitionLocator.getRouteDefinitions()
                .collectMap(RouteDefinition::getId);
        Mono<List<Route>> routes = this.routeLocator.getRoutes().collectList();

        return Mono.zip(routeDefs, routes).map(tuple -> {
            Map<String, RouteDefinition> defs = tuple.getT1();
            List<Route> routeList = tuple.getT2();
            List<Map<String, Object>> allRoutes = new ArrayList<>();

            routeList.forEach(route -> {
                HashMap<String, Object> r = new HashMap<>();
                r.put("route_id", route.getId());
                r.put("order", route.getOrder());
                if (defs.containsKey(route.getId())) {
                    r.put("route_definition", defs.get(route.getId()));
                } else {
                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("predicate", route.getPredicate().toString());
                    if (!route.getFilters().isEmpty()) {
                        ArrayList<String> filters = new ArrayList<>();
                        for (GatewayFilter filter : route.getFilters()) {
                            filters.add(filter.toString());
                        }
                        obj.put("filters", filters);
                    }
                    if (!obj.isEmpty()) {
                        r.put("route_object", obj);
                    }
                }
                allRoutes.add(r);
            });

            return allRoutes;
        });
    }

    public Mono<ResponseEntity<RouteDefinition>> getRouteById(String id) {
        return this.routeDefinitionLocator.getRouteDefinitions()
                .filter(route -> route.getId().equals(id))
                .singleOrEmpty()
                .map(route -> ResponseEntity.ok(route))
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @PostConstruct
    public void initDefaultRoutes() {
        redisTemplate.opsForHash().values(GATEWAY_ROUTES).forEach(routeDefinition -> add(JSONObject.parseObject(routeDefinition.toString(), RouteDefinition.class)));
    }


    private RouteDefinition assembleRouteDefinition(RouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        List<PredicateDefinition> predicates = new ArrayList<>();
        definition.setId(gwdefinition.getId());

        gwdefinition.getPredicates().forEach(p -> {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(p.getArgs());
            predicate.setName(p.getName());
            predicates.add(predicate);
        });

        definition.setPredicates(predicates);
        String uriString = gwdefinition.getUri().toString();
        if (StringUtils.isEmpty(uriString)) {
            return definition;
        }

        String schema = gwdefinition.getUri().getScheme();
        definition.setUri(LB.toString().toLowerCase().equals(schema) ? gwdefinition.getUri()
                : UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri().toString()).build().toUri());
        return definition;
    }


}
