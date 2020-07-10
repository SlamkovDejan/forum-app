package mk.ukim.finki.emt.forum.forummanagement.port.client;

import mk.ukim.finki.emt.forum.forummanagement.aplication.UserService;
import mk.ukim.finki.emt.forum.forummanagement.domain.value.UserId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UserServiceClient implements UserService {

    private final RestTemplate restTemplate;
    private final String serverUrl;

    public UserServiceClient(@Value("${app.product-catalog.url}") String serverUrl,
                             @Value("${app.product-catalog.connect-timeout-ms}") int connectTimeout,
                             @Value("${app.product-catalog.read-timeout-ms}") int readTimeout){
        this.serverUrl = serverUrl;
        this.restTemplate = new RestTemplate();

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(connectTimeout);
        requestFactory.setReadTimeout(readTimeout);

        restTemplate.setRequestFactory(requestFactory);
    }

    private UriComponentsBuilder uri() {
        return UriComponentsBuilder.fromUriString(serverUrl);
    }

    @Override
    public Stream<UserId> findAllUserIdsByRoleId(UUID roleId) {
        try {
            // /api/users/role/{roleId}/ids
            return Objects.requireNonNull(
                    restTemplate.exchange(
                            uri().path(String.format("/api/users/role/%s/ids", roleId.toString())).build().toUri(),
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<List<UUID>>() {}
                    ).getBody()
            ).stream().map(UserId::new);
        } catch (Exception ex) {
            System.err.printf("Error retrieving product by id; %s\n", ex);
            return null;
        }
    }

}
