import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class Main {

    public static void totalPriceByOrder(double[][] data) {
        Map<String, Double> totalPriceByOrder = new HashMap<>();

        // totalPrice hesaplama
        for (double[] row : data) {
            String order = String.valueOf((int) row[0]);
            double totalPrice = row[2] * row[3];

            // Order Bazlı totalPrice hesaplama
            if (totalPriceByOrder.containsKey(order)) {
                totalPrice += totalPriceByOrder.get(order);
            }

            totalPriceByOrder.put(order, totalPrice);
        }

        System.out.println("Siparis No\tToplam Tutar");
        for (Map.Entry<String, Double> entry : totalPriceByOrder.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }
    }

    public static void totalPriceSum(double[][] data) {
        double totalPricesSum = 0;
        int totalItemsCount = 0;

        for (double[] row : data) {
            double price = row[3];
            int quantity = (int) row[2];

            totalPricesSum += price * quantity;
            totalItemsCount += quantity;
        }

        double averagePrice = totalPricesSum / totalItemsCount;

        System.out.println("Butun Mallarin Ortalama Fiyati: " + averagePrice);
    }

    public static void quantitiesByProductAndOrder(double[][] data) {
        Map<Integer, Map<Integer, Integer>> quantitiesByProductAndOrder = new HashMap<>();

        for (double[] row : data) {
            int order = (int) row[0];
            int product = (int) row[1];
            int quantity = (int) row[2];

            if (quantitiesByProductAndOrder.containsKey(product)) {
                Map<Integer, Integer> quantitiesByOrder = quantitiesByProductAndOrder.get(product);
                if (quantitiesByOrder.containsKey(order)) {
                    quantity += quantitiesByOrder.get(order);
                }
                quantitiesByOrder.put(order, quantity);
            } else {
                Map<Integer, Integer> quantitiesByOrder = new HashMap<>();
                quantitiesByOrder.put(order, quantity);
                quantitiesByProductAndOrder.put(product, quantitiesByOrder);
            }

            /*
             * quantitiesByProductAndOrder.putIfAbsent(product, new HashMap<>());
             * Map<Integer, Integer> quantitiesByOrder =
             * quantitiesByProductAndOrder.get(product);
             * 
             * quantitiesByOrder.put(order, quantitiesByOrder.getOrDefault(order, 0) +
             * quantity);
             */

        }
        /*
         * for (Map.Entry<Integer, Map<Integer, Integer>> entry :
         * quantitiesByProductAndOrder.entrySet()) {
         * int product = entry.getKey();
         * Map<Integer, Integer> quantitiesByOrder = entry.getValue();
         * 
         * for (Map.Entry<Integer, Integer> subEntry : quantitiesByOrder.entrySet()) {
         * int order = subEntry.getKey();
         * int quantity = subEntry.getValue();
         * 
         * System.out.println(product + "\t" + order + "\t\t" + quantity);
         * }
         * }
         */

        System.out.println("Mal No\tSiparis No\tAdet");
        quantitiesByProductAndOrder.forEach((product, quantitiesByOrder) -> quantitiesByOrder
                .forEach((order, quantity) -> System.out.println(product + "\t" + order + "\t\t" + quantity)));

    }

    public static void averagePriceByProduct(double[][] data) {
        Map<Integer, Double> averagePriceByProduct = new HashMap<>();
        Map<Integer, Integer> quantitiesByProduct = new HashMap<>();

        for (double[] row : data) {
            int product = (int) row[1];
            double price = row[3];
            int quantity = (int) row[2];

            if (averagePriceByProduct.containsKey(product)) {
                int totalQuantity = quantitiesByProduct.get(product) + quantity;
                double totalPrice = averagePriceByProduct.get(product) * quantitiesByProduct.get(product)
                        + price * quantity;
                averagePriceByProduct.put(product, totalPrice / totalQuantity);
                quantitiesByProduct.put(product, totalQuantity);
            } else {
                averagePriceByProduct.put(product, price);
                quantitiesByProduct.put(product, quantity);
            }
        }

        System.out.println("Mal No\tOrtalama Fiyat");
        for (Map.Entry<Integer, Double> entry : averagePriceByProduct.entrySet()) {
            System.out.println(entry.getKey() + "\t" + entry.getValue());
        }
    }

    public static void getExample(String get) {

        try {
            // İstekte bulunacak URL
            URL url = new URL("http://localhost:8080/api/v1/companies/companylist");

            // URL ile bağlantı oluşturma
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // İstek metodunu GET olarak belirleyelim
            connection.setRequestMethod(get);

            // İstek yanıt kodunu alalım
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Yanıt mesajını okuyalım
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Yanıtı ekrana yazdıralım
            System.out.println("Response: " + response.toString());

            // Bağlantıyı kapat
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void postExample(String post) {
        try {
            // İstekte bulunacağımız URL'yi belirtelim
            URL url = new URL("http://localhost:8080/api/v1/companies/newcompany");

            // URL ile bağlantı oluşturalım
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // İstek metodunu POST olarak belirleyelim
            connection.setRequestMethod(post);

            // İstek başlıklarını (header) belirleyelim
            connection.setRequestProperty("Content-Type", "application/json");

            // İçeriği gönderilecek veriyi oluşturalım
            String body = "{\"companyName\":\"test10\"}";

            // Veriyi yazma ve gönderme işlemlerini yapalım
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(body.getBytes());
            outputStream.flush();
            outputStream.close();

            // İstek yanıt kodunu alalım
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // Yanıt mesajını okuyalım
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            // Yanıtı ekrana yazdıralım
            System.out.println("Response: " + response.toString());

            // Bağlantıyı kapat
            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        // Verilen tablo verileri
        double[][] data = {
                { 1000, 2000, 12, 100.51 },
                { 1000, 2001, 31, 200 },
                { 1000, 2002, 22, 150.86 },
                { 1000, 2003, 41, 250 },
                { 1000, 2004, 55, 244 },

                { 1001, 2001, 88, 44.531 },
                { 1001, 2002, 121, 88.11 },
                { 1001, 2004, 74, 211 },
                { 1001, 2002, 14, 88.11 },

                { 1002, 2003, 2, 12.1 },
                { 1002, 2004, 3, 22.3 },
                { 1002, 2003, 8, 12.1 },
                { 1002, 2002, 16, 94 },
                { 1002, 2005, 9, 44.1 },
                { 1002, 2006, 19, 90 }
        };
        System.out.println("\n a. Uc siparisteki mallarin toplam tutarinin ciktisini veren java kodu.");
        totalPriceByOrder(data);
        System.out.println("\n b. Uc siparisteki butun mallarin ortalama fiyatini bulan java kodu");
        totalPriceSum(data);
        System.out.println("\n c. Uc siparisteki butun mallarin tek tek mal bazli ortalama fiyatini bulan java kodu. ");
        averagePriceByProduct(data);
        System.out.println(
                "\n d. Tek tek mal bazli, mallarin hangi siparislerde kac adet olduğunun ciktisini veren java kodu. ");
        quantitiesByProductAndOrder(data);

        System.out.println(
                "\n POST Example");
        postExample("POST");
        System.out.println(
                "\n GET Example");
        getExample("GET");

    }

}