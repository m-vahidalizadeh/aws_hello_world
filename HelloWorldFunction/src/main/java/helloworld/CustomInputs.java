package helloworld;

import java.util.List;

public class CustomInputs {

    public double handler(List<Integer> nums) {
        return nums.stream().mapToInt(Integer::intValue).average().getAsDouble();
    }

}
