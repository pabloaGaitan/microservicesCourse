package com.microservicios.zuul.filter;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


@Component
public class PostTiempoTranscurridoFilter extends ZuulFilter{
	
	private static Logger log = org.slf4j.LoggerFactory.getLogger(PostTiempoTranscurridoFilter.class);
	
	@Override
	public boolean shouldFilter() {
		//para verificar si se debe correr el filtro
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		// logica
		RequestContext ctx = RequestContext.getCurrentContext();
		HttpServletRequest request = ctx.getRequest();
		log.info("entrando a post");
		Long tiempoTranscurrido = System.currentTimeMillis() - ((Long) request.getAttribute("tiempoInicio"));
		log.info("Tiempo transcurrido en segundos:",(tiempoTranscurrido.doubleValue()/1000.00));
		return null;
	}

	@Override
	public String filterType() {
		return "post";
	}

	@Override
	public int filterOrder() {
		return 1;
	}

}
