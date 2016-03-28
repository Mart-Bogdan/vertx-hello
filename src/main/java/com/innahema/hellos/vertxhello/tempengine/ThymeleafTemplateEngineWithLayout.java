package com.innahema.hellos.vertxhello.tempengine;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.templ.TemplateEngine;
import io.vertx.ext.web.templ.ThymeleafTemplateEngine;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Bogdan Mart on 28.03.2016.
 */
public class ThymeleafTemplateEngineWithLayout implements TemplateEngine {
    private static final Charset UTF_8 = Charset.forName("UTF-8");

    private TemplateEngine baseEngine = ThymeleafTemplateEngine.create();
    private String layoutTemplateFileName;
    private Map<String,Boolean> useLayoutCache = new HashMap<>();

    public ThymeleafTemplateEngineWithLayout(String layoutTemplateFileName) {

        this.layoutTemplateFileName = layoutTemplateFileName;
    }

    public void render(RoutingContext context, String templateFileName, Handler<AsyncResult<Buffer>> handler) {
        final Boolean[] useLayout = {useLayoutCache.get(templateFileName)};

        Runnable doRenderFile = ()->{

            if(useLayout[0]){
                context.put("com.innahema.hellos.vertxhello.tempengine.VIEW", templateFileName);
            }

            baseEngine.render(
                    context,
                    useLayout[0] ? layoutTemplateFileName:templateFileName,
                    handler
            );
        };

        if(useLayout[0] ==null){
            context.vertx().fileSystem().readFile(templateFileName,res->{
               if(res.failed())
                   handler.handle(res);

                String content = res.result().toString(UTF_8);
                useLayout[0] = content.contains("th:fragment=\"");
                useLayoutCache.put(templateFileName,useLayout[0]);

                doRenderFile.run();
            });
        }
        else {
            doRenderFile.run();
        }
    }
}
