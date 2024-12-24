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
        // 操作人
        String name = "";
        Account user = TokenUtils.getCurrentUser();
        if (ObjectUtil.isNotNull(user)) {
            name = user.getName();
        }

        //角色
        String role = "";

        // 操作人IP
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getRemoteAddr();

        // 执行具体的接口
        Result result = (Result) joinPoint.proceed();

        Object data =result.getData();
        if (data instanceof Admin admin) {
            name = admin.getName();
            role = admin.getRole();
        }
        else if (data instanceof Doctor doctor) {
            name = doctor.getName();
            role = doctor.getRole();
        }
        else if (data instanceof Nurse nurse) {
            name = nurse.getName();
            role = nurse.getRole();
        }
        else if (data instanceof User user1) {
            name = user1.getName();
            role = user1.getRole();
        }


        // 再去往日志表里写一条日志记录
        Log log = new Log(null, content, name, time, role, ip);
        logService.add(log);

        // 你可以走了，去返回前台报到吧~
        return result;
    }
}
