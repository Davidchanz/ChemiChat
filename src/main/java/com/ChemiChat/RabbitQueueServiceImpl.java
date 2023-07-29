package com.ChemiChat;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RabbitQueueServiceImpl implements RabbitQueueService {
    @Autowired
    private AmqpAdmin rabbitAdmin;
    @Autowired
    private RabbitListenerEndpointRegistry rabbitListenerEndpointRegistry;

    @Override
    public void addNewQueue(String queueName, String exchangeName, String routingKey) {
        Queue queue = new Queue(queueName, true, false, false);
        Binding binding = new Binding(
                queueName,
                Binding.DestinationType.QUEUE,
                exchangeName,
                routingKey,
                null
        );
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(binding);
        this.addQueueToListener(exchangeName,queueName);
    }

    @Override
    public void addQueueToListener(String listenerId, String queueName) {
        //log.info("adding queue : " + queueName + " to listener with id : " + listenerId);
        if (!checkQueueExistOnListener(listenerId,queueName)) {
            this.getMessageListenerContainerById(listenerId).addQueueNames(queueName);
            //log.info("queue ");
        } else {
            //log.info("given queue name : " + queueName + " not exist on given listener id : " + listenerId);
        }
    }

    @Override
    public void removeQueueFromListener(String listenerId, String queueName) {
        //log.info("removing queue : " + queueName + " from listener : " + listenerId);
        if (checkQueueExistOnListener(listenerId,queueName)) {
            this.getMessageListenerContainerById(listenerId).removeQueueNames(queueName);
            //log.info("deleting queue from rabbit management");
            this.rabbitAdmin.deleteQueue(queueName);
        } else {
            //log.info("given queue name : " + queueName + " not exist on given listener id : " + listenerId);
        }
    }

    @Override
    public Boolean checkQueueExistOnListener(String listenerId, String queueName) {
        try {
            //log.info("checking queueName : " + queueName + " exist on listener id : " + listenerId);
            //log.info("getting queueNames");
            String[] queueNames = this.getMessageListenerContainerById(listenerId).getQueueNames();
            //log.info("queueNames : " + new Gson().toJson(queueNames));
            if (queueNames != null) {
                //log.info("checking " + queueName + " exist on active queues");
                for (String name : queueNames) {
                    //log.info("name : " + name + " with checking name : " + queueName);
                    if (name.equals(queueName)) {
                        //log.info("queue name exist on listener, returning true");
                        return Boolean.TRUE;
                    }
                }
                return Boolean.FALSE;
            } else {
                //log.info("there is no queue exist on listener");
                return Boolean.FALSE;
            }
        } catch (Exception e) {
            //log.error("Error on checking queue exist on listener");
            //log.error("error message : " + ExceptionUtils.getMessage(e));
            //log.error("trace : " + ExceptionUtils.getStackTrace(e));
            return Boolean.FALSE;
        }
    }

    private AbstractMessageListenerContainer getMessageListenerContainerById(String listenerId) {
        //log.info("getting message listener container by id : " + listenerId);
        System.out.println(this.rabbitListenerEndpointRegistry
                .getListenerContainerIds());
        return ((AbstractMessageListenerContainer) this.rabbitListenerEndpointRegistry
                .getListenerContainer(listenerId)
        );
    }
}