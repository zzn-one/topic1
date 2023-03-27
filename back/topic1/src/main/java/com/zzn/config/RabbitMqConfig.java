package com.zzn.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zzn
 * @create 2023-02-01 21:06
 */
@Configuration
public class RabbitMqConfig {
    public static final String EXCHANGE_NAME = "topic1_exchange";
    public static final String QUEUE_NAME = "topic1_queue";
    public static final String ROUTING_NAME_TASK_ADD = "topicTask.add";
    public static final String ROUTING_NAME_TASK_UPDATE = "topicTask.update";
    public static final String ROUTING_NAME_STUDENT_UPDATE = "student.update";
    public static final String ROUTING_NAME_TUTOR_UPDATE = "tutor.update";
    public static final String ROUTING_NAME_ADMIN_UPDATE = "admin.update";

    /*
    定义交换机
    */
    @Bean("topicTask_exchange")
    public Exchange exchange() {
        return ExchangeBuilder
                .topicExchange(EXCHANGE_NAME)
                .durable(true)
                .build();
    }

    /*
    定义 task add 消息队列
    */
    @Bean("topicTask_add_queue")
    public Queue queueAddTask() {
        return QueueBuilder
                .durable(QUEUE_NAME + "_task_add")
                .build();
    }

    /*
    绑定交换机和 add 队列
    */
    @Bean
    public Binding bindingTaskAdd(@Qualifier("topicTask_exchange") Exchange exchange, @Qualifier("topicTask_add_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_NAME_TASK_ADD + ".*")
                .noargs();
    }

    /*
    定义 task update 消息队列
    */
    @Bean("topicTask_update_queue")
    public Queue queueUpdateTask() {
        return QueueBuilder
                .durable(QUEUE_NAME + "_task_update")
                .build();
    }

    /*
    绑定交换机和 task update 队列
    */
    @Bean
    public Binding bindingTaskUpdate(@Qualifier("topicTask_exchange") Exchange exchange, @Qualifier("topicTask_update_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_NAME_TASK_UPDATE + ".*")
                .noargs();
    }


    /*
    定义 student update 消息队列
    */
    @Bean("topicTask_student_update_queue")
    public Queue queueStudentUpdate() {
        return QueueBuilder
                .durable(QUEUE_NAME + "_student_update")
                .build();
    }

    /*
    绑定交换机和 student update 队列
    */
    @Bean
    public Binding bindingStudent(@Qualifier("topicTask_exchange") Exchange exchange, @Qualifier("topicTask_student_update_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_NAME_STUDENT_UPDATE + ".*")
                .noargs();
    }

    /*
    定义 tutor update 消息队列
    */
    @Bean("topicTask_tutor_update_queue")
    public Queue queueTutorUpdate() {
        return QueueBuilder
                .durable(QUEUE_NAME + "_tutor_update")
                .build();
    }

    /*
    绑定交换机和 tutor update 队列
    */
    @Bean
    public Binding bindingTutor(@Qualifier("topicTask_exchange") Exchange exchange, @Qualifier("topicTask_tutor_update_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_NAME_TUTOR_UPDATE + ".*")
                .noargs();
    }

    /*
    定义 admin update 消息队列
    */
    @Bean("topicTask_admin_update_queue")
    public Queue queueAdminUpdate() {
        return QueueBuilder
                .durable(QUEUE_NAME + "_admin_update")
                .build();
    }

    /*
    绑定交换机和 tutor update 队列
    */
    @Bean
    public Binding bindingAdmin(@Qualifier("topicTask_exchange") Exchange exchange, @Qualifier("topicTask_admin_update_queue") Queue queue) {
        return BindingBuilder
                .bind(queue)
                .to(exchange)
                .with(ROUTING_NAME_ADMIN_UPDATE+ ".*")
                .noargs();
    }
}
