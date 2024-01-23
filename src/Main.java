import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        }

        System.out.println("Mal No\tSipariş No\tAdet");
        for (Map.Entry<Integer, Map<Integer, Integer>> entry : quantitiesByProductAndOrder.entrySet()) {
            int product = entry.getKey();
            Map<Integer, Integer> quantitiesByOrder = entry.getValue();

            for (Map.Entry<Integer, Integer> subEntry : quantitiesByOrder.entrySet()) {
                int order = subEntry.getKey();
                int quantity = subEntry.getValue();

                System.out.println(product + "\t" + order + "\t\t" + quantity);
            }
        }
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

    public static void httpRequestFunc() {
        try {
            // GET Request örneği
            URL url = new URL("http://localhost:8080//data");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response;
            StringBuilder responseContent = new StringBuilder();

            while ((response = reader.readLine()) != null) {
                responseContent.append(response);
            }
            reader.close();

            System.out.println("Response Content: " + responseContent.toString());

            connection.disconnect();

            // POST Request örneği
            url = new URL("http://localhost:8080//data");
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "param1=value1&param2=value2";
            connection.getOutputStream().write(postData.getBytes());

            responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            responseContent = new StringBuilder();

            while ((response = reader.readLine()) != null) {
                responseContent.append(response);
            }
            reader.close();

            System.out.println("Response Content: " + responseContent.toString());

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

        totalPriceByOrder(data);
        totalPriceSum(data);
        averagePriceByProduct(data);
        quantitiesByProductAndOrder(data);
        httpRequestFunc();

    }

}