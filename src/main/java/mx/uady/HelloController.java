package mx.uady;

import java.util.concurrent.TimeUnit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

@RestController
public class HelloController{

  private final RabbitTemplate rabbitTemplate;
    private final Receiver receiver;

    public HelloController(Receiver receiver, RabbitTemplate rabbitTemplate) {
        this.receiver = receiver;
        this.rabbitTemplate = rabbitTemplate;
    }

  @GetMapping("/hello")
  public void getHelloWorld(@RequestBody String mensaje) throws Exception{
    // parsing file "JSONExample.json" 
    Object obj = new JSONParser().parse(mensaje); 
          
    // typecasting obj to JSONObject 
    JSONObject jo = (JSONObject) obj; 

    System.out.println("Enviando mensaje...");
    rabbitTemplate.convertAndSend(Aplicacion.topicExchangeName, "foo.bar.baz", jo.get("mensaje"));
    receiver.getLatch().await(10000, TimeUnit.MILLISECONDS);
  }

}
