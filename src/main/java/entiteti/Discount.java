package entiteti;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static Datoteke.ReadingFiles.logger;

public interface Discount {

    default List<Item> calculateCheapestItem(List<Item> itemsList, BigDecimal discount){
        List<Item> cheapestItems = new ArrayList<>(0);

        Optional<BigDecimal> max = itemsList.stream()
                .map(i -> i.getPrice())
                .max(BigDecimal::compareTo);

        Optional<BigDecimal> cheapestItem = itemsList.stream().map(i -> i.getPrice().multiply(discount).divide(BigDecimal.valueOf(100))).min(BigDecimal::compareTo);
        if (cheapestItem.isPresent()){
            for (Item item:
                    itemsList) {
                Optional<BigDecimal> rangeUp = Optional.of(cheapestItem.get().multiply(BigDecimal.valueOf(0.5)));
                Optional<BigDecimal> itemsPrice = Optional.of(item.getPrice());
                if(itemsPrice.get().compareTo(rangeUp.get())<0){
                    System.out.println(rangeUp + "Manji je: " + itemsPrice);
                    cheapestItems.add(item);
                }
            }
        }

        return cheapestItems;
    }

    default List<Item> distributeDiscountToItems(List<Item> itemsList){

        SecureRandom rand = new SecureRandom();
        int upperbound = itemsList.size();

        Random random = new Random();
        int upperbound2 = 25;
        float float_random = rand.nextFloat();
        BigDecimal discount_random = BigDecimal.valueOf(random.nextInt(upperbound));

        Set<Item> setList = new HashSet<>(0);

        int upperMaxItems = itemsList.size() - itemsList.size()/2 - 1;
        for (int i = 0; i < upperMaxItems; i++) {
            int int_randomItemId = rand.nextInt(upperbound);
            for (Item item:
                 itemsList) {
                if(item.getId().equals((long) int_randomItemId)){
                    BigDecimal oldPrice = item.getPrice();
                    BigDecimal newPrice = oldPrice.subtract(oldPrice.multiply(BigDecimal.valueOf(float_random)));
                    newPrice = newPrice.setScale(2,  RoundingMode.HALF_EVEN);
                    item.setPrice(newPrice);
                    setList.add(item);
                    logger.info("Doslo je do izmjena cijene: " + item);
                }
            }
        }

        List<Item> list = new ArrayList<>(setList);

        return list;
    }
}
