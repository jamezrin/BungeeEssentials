import org.junit.Test;

public class StringTest {
    @Test
    public void test() {
        String test = "[Report] %reporter% reported %reporter% for %report%. %teleport%";
        int index = test.lastIndexOf("%teleport%");
        System.out.println(index);
        String left = test.substring(0, index);
        System.out.println(left);
        String right = test.substring(index + "%teleport%".length(), test.length());
        System.out.println(right);
    }
}
