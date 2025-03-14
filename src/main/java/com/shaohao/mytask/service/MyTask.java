package com.shaohao.mytask.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.shaohao.mytask.dto.user.MyUser;
import com.shaohao.mytask.entity.TaskConfig;
import com.shaohao.mytask.enums.wofUrlEnum;
import com.shaohao.mytask.mapper.TaskConfigMapper;
import com.shaohao.mytask.utils.DateUtils;
import com.shaohao.mytask.utils.HTTP;
import com.shaohao.mytask.utils.Web3Until;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 友情链接 服务实现类
 * </p>
 *
 * @author shaohao
 * @since 2023-06-17
 */
@Component
public class MyTask {

    private static final Logger logger = LoggerFactory.getLogger(MyTask.class);

    @Autowired
    private MyTaskService myTaskService;
    @Autowired
    private TaskConfigMapper taskConfigMapper;
    @Autowired
    private TaskExecutor  taskExecutor;
    @Autowired
    private HTTP http;

    @Autowired
    private SolMonitor solMonitor;


//
//    private static final Logger logger = LoggerFactory.getLogger("MyLogger");
//    private static final Logger  myLogger = LoggerFactory.getLogger(GameActionServiceImpl.class);

  //  @Scheduled(fixedRate=60000)
    public void doTask() throws IOException {

//        test();
//         solMonitor.solMonitor("8wwZdqZomnMT8bKbh6zaJcgHFsNi7CcMbp1WtqCHBTnZ");
//        solMonitor.solMonitor("Bb3XyXJvZ9L3c6tSpubLjZNUXZh8JoCWEyYV9nApKjWr");
//        solMonitor.solMonitor("CjeBRyWfpjGo9KSo2erb2hihGiYordjnt876BK5QgoWb");
//        solMonitor.solMonitor("E2deRddezgHHbZ2KWVnRGDEHTuJt2Fvjy6oEm5vnvoCP");




    }

    public void main() {
        List<TaskConfig> list = taskConfigMapper.selectList(new QueryWrapper<>());
        for (TaskConfig entity: list){
            taskExecutor.execute(() -> {
                myTaskService.main(entity);
            });
        }

    }



    /**
     * <p>
     * 读取控制台字符串
     * </p>
     */
    public static String scanner(String tip) {
        Scanner scanner = new Scanner(System.in);
        String ipt = "";
        boolean validInput = false;
        while (!validInput) {
            StringBuilder help = new StringBuilder();
            help.append("请输入" + tip + "：");
            System.out.println(help.toString());
            if (scanner.hasNext()) {
                ipt = scanner.nextLine();
                if (ipt != null) {
                    validInput = true;
                }
            }
            if (!validInput){
                System.out.println("请重新输入" + tip + "：");
            }

        }
//        scanner.close();
        return ipt;
    }

    /**
     * <p>
     * 读取控制台int
     * </p>
     */
    public static int scannerInt(String tip) {
        Scanner scanner = new Scanner(System.in);
        int number = 0;
        boolean validInput = false;
        while (!validInput) {
            System.out.print("请输入" + tip + "：");
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                System.out.println("输入不能为空，请重新输入。");
                continue;
            }

            try {
                number = Integer.parseInt(input);
                validInput = true;
            } catch (NumberFormatException e) {
                System.out.println("输入无效，请重新输入。");
            }
        }
//        scanner.close();
        return number;
    }




    public void sleep(int second) {
        try {
            Thread.sleep(1000*second); // 休眠十秒（10000毫秒）
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
