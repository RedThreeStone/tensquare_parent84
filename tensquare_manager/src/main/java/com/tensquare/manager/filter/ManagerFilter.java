package com.tensquare.manager.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

public class ManagerFilter extends ZuulFilter {
    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {
        RequestContext context = RequestContext.getCurrentContext();
        HttpServletRequest request = context.getRequest();
        if(request.getMethod().equals("OPTIONS")){
            return  null;
        }
        if(request.getRequestURL().toString().contains("login")){
            return null;
        }
        String authorization = request.getHeader("Authorization");
        if(StringUtils.isNotEmpty(authorization) && authorization.startsWith("Bearer  ")){
            String s = authorization.substring(7);
            try {
                Claims claims = jwtUtil.parseJWT(s);
                String roles= (String) claims.get("Roles");
                if("admin".equals(roles)){
                    context.addZuulRequestHeader("Authorization",authorization);
                    return null;
                }

            }catch (Exception e){
                e.printStackTrace();

            }
            context.setResponseStatusCode(403);
            context.setResponseBody("权限不足，请登录");
            context.setSendZuulResponse(false);
            context.getResponse().setContentType("text/html;charset=UTF-8");


        }

        return null;
    }
}
