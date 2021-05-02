package rest.demo.controller;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import rest.demo.model.User;

import java.util.*;

public class RESTController {
    private static final String GET_USERS_URL = "http://91.241.64.178:7081/api/users";
    private static final String GET_USER_UPDATE_BY_ID_URL = "http://91.241.64.178:7081/api/users";
    private static final String GET_USER_DELETE_BY_ID_URL = "http://91.241.64.178:7081/api/users/{id}";
    private static final String GET_USERS_CREATE_URL = "http://91.241.64.178:7081/api/users";

    private static final RestTemplate restTemplate = new RestTemplate();

    public static void main(String[] args) {
        RESTController controller = new RESTController();

        controller.getUsers();
    }

    private void getUsers() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<String> user = new HttpEntity<>("parameters", headers);
        //Получить список всех пользователей
        //get
        ResponseEntity<String> result = restTemplate.exchange(GET_USERS_URL, HttpMethod.GET, user, String.class);//соответствующий метод, используемый для установки заголовков запросов.

        List<String> cookie = result.getHeaders().get("Set-Cookie");
        System.out.println("All users: ");
        System.out.println(result);//вывод пользователей
        System.out.println(cookie);//set-cookie и session id

        //Сохранить пользователя с id = 3, name = James, lastName = Brown, age = на ваш выбор
        //post
        if (cookie != null) {
            headers.set("Cookie", String.join(";", cookie));
        }
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        User newUser = new User(3L, "James", "Brown", (byte) 66);
        HttpEntity<User> requestBody = new HttpEntity<>(newUser, headers);

        System.out.println("Add user: ");
        ResponseEntity<String> resultAddUser = restTemplate.postForEntity(GET_USERS_CREATE_URL, requestBody, String.class);
        /*PostForEntity(..) метод оболочки, который еще больше облегчает использование для выполнения вызовов REST.
        Вы указываете тип запроса в самом имени метода(getForEntity, postForEntity),
        поэтому не нужно упоминать тип запроса в параметре. Название метода само по себе
        становится понятным.*/

        System.out.println(resultAddUser.getBody());//first code


        /*Изменить пользователя с id = 3. Необходимо поменять name на Thomas,
        а lastName на Shelby. В случае успеха вы получите еще одну часть кода.*/
        //put
        if (cookie != null) {
            headers.set("Cookie", String.join(";", cookie));
        }

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        Map<String, Integer> man = new HashMap<>();
        man.put("id", 3);
        User editUser = new User((long) 3, "Thomas", "Shelby", (byte) 66);
        HttpEntity<User> editHttp = new HttpEntity<>(editUser, headers);

        //изменение
        System.out.println("Update user: ");
        ResponseEntity<String> responseEntity = restTemplate.exchange(GET_USER_UPDATE_BY_ID_URL, HttpMethod.PUT, editHttp, String.class, man);
        System.out.println(responseEntity.getBody());//second code

        /* Удалить пользователя с id = 3. */
        if (cookie != null) {
            headers.set("Cookie", String.join(";", cookie));
        }
        Map<String, Integer> omgMan = new HashMap<>();
        omgMan.put("id", 3);
        HttpEntity<User> delHttp = new HttpEntity<>(editUser, headers);
        ResponseEntity<String> delUserOmg = restTemplate.exchange(GET_USER_DELETE_BY_ID_URL, HttpMethod.DELETE, delHttp, String.class, omgMan);
        //удаление
        System.out.println(delUserOmg.getBody());// ANSWER 
    }
}
