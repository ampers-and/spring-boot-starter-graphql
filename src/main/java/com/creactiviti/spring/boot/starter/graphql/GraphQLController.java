package com.creactiviti.spring.boot.starter.graphql;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import reactor.core.publisher.Flux;

/**
 * @author Arik Cohen
 * @since Feb 08, 2018
 */
@CrossOrigin
@RestController
public class GraphQLController {

  private final GraphQL graphql;
  
  private final Logger log = LoggerFactory.getLogger(getClass());
  
  public GraphQLController (GraphQL aGraphQL) {
    graphql = aGraphQL;
  }
  
  @PostMapping(value="/graphql", consumes="application/json", produces="text/event-stream")
  public Flux<?> reactiveGrapql (@RequestBody Map<String,Object> aQuery) {
    return graphqlInternal(aQuery).getData();
  }
  
  @PostMapping(value="/graphql", consumes="application/json", produces="application/json")
  public ExecutionResult graphql (@RequestBody Map<String,Object> aQuery) {
    return graphqlInternal(aQuery);
  }
  
  private ExecutionResult graphqlInternal (@RequestBody Map<String,Object> aQuery) {
    Assert.notNull(graphql, "graphql not defined");
    
    long now = System.currentTimeMillis();
    
    ExecutionResult result = graphql.execute(ExecutionInput.newExecutionInput()
                                                           .query((String)aQuery.get("query"))
                                                           .variables((Map<String, Object>) aQuery.get("variables"))
                                                           .build());
    
    log.debug("{} [{}ms]",aQuery.get("query"),(System.currentTimeMillis()-now));
    
    return result;
  }  
  
}
