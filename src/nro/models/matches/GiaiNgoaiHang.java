package nro.models.matches;

import nro.server.ServerManager;
import java.util.ArrayList;
import java.util.List;

public class GiaiNgoaiHang implements Runnable {

    private static GiaiNgoaiHang I;

    public static GiaiNgoaiHang gI() {
        if (GiaiNgoaiHang.I == null) {
            GiaiNgoaiHang.I = new GiaiNgoaiHang();
            new Thread(GiaiNgoaiHang.I, "Update giải đấu ngoại hạng").start();
        }
        return GiaiNgoaiHang.I;
    }

    private final List<Long> subscribers;

    public GiaiNgoaiHang() {
        this.subscribers = new ArrayList<>();
    }

    @Override
    public void run() {
        this.update();
    }

    private void update() {
        while (ServerManager.isRunning) {
            try {
                long startTime = System.currentTimeMillis();

                // Thực hiện hoạt động cụ thể ở đây
                System.out.println("Updating...");

                long elapsedTime = System.currentTimeMillis() - startTime;
                long sleepTime = 1000 - elapsedTime;
                sleepTime = Math.max(0, sleepTime);

                Thread.sleep(sleepTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break; // Thoát khỏi vòng lặp nếu bị gián đoạn
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}

/**
 * Vui lòng không sao chép mã nguồn này dưới mọi hình thức. Hãy tôn trọng tác
 * giả của mã nguồn này. Xin cảm ơn! - GirlBeo
 */
