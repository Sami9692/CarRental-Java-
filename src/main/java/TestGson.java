import com.carrental.api.ApiMain;
import com.carrental.model.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestGson {
    public static void main(String[] args) {
        try {
            Payment p = new Payment();
            p.setPaymentId(1);
            p.setReservationId(8);
            p.setAmount(new BigDecimal("100.00"));
            p.setPaymentDate(LocalDateTime.now());
            p.setPaymentStatus("completed");
            
            String json = ApiMain.GSON.toJson(p);
            System.out.println("Serialization success: " + json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
