package com.example.common;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.*;
import com.example.service.LogService;
import com.example.utils.TokenUtils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Aspect
public class LogAspect {

    @Resource
    private LogService logService;

    @Around("@annotation(autoLog)")
    public Object doAround(ProceedingJoinPoint joinPoint, AutoLog autoLog) throws Throwable {
        // 操作内容，我们在注解里已经定义了value()，然后再需要切入的接口上面去写上对应的操作内容即可
        String content = autoLog.value();
        // 操作时间（当前时间）
        String time = DateUtil.now();
        // 操作人IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();

        // 操作人及其角色（直接从 Token 中获取）
        String name = "";
        String role = "";
        Account user = TokenUtils.getCurrentUser();
        if (ObjectUtil.isNotNull(user)) {
            name = user.getName();
            role = user.getRole();  // 假设 Account 类有 getRole() 方法
        }

        // 执行接口并获取结果
        Result result = (Result) joinPoint.proceed();

        // 删除从 Result.data 中提取角色的逻辑（不再需要）

        // 记录日志
        Log log = new Log(null, content, name, time, role, ip);
        logService.add(log);

        return result;
    }

}
