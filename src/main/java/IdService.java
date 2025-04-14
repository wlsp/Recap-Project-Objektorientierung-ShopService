import java.util.UUID;

public class IdService {
    public String generateOrderId(){
        return UUID.randomUUID().toString();
    }
}
