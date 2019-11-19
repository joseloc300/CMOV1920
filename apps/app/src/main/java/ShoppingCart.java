import java.util.ArrayList;

public class ShoppingCart {
    private static ArrayList<Item> cart = new ArrayList<>();

    public int itemExists(String id) {
        int ret = -1;

        for (int i = 0; i < cart.size(); i++) {
            if (cart.get(i).getId().equals(id)) {
                ret = i;
                break;
            }
        }

        return ret;
    }

    public int addItem(Item i) {
        int index = itemExists(i.getId());
        int totalCount = 0;

        if (index != -1) {
            cart.get(index).addCount(i.getCount());
            totalCount = cart.get(index).getCount();
        }
        else {
            cart.add(i);
            totalCount = i.getCount();
        }

        return totalCount;
    }

    public Boolean removeItem(String id) {
        int index = itemExists(id);

        if (index != -1) {
            cart.remove(index);
            return true;
        }
        else
            return false;
    }

    public Boolean editItemCount(String id, int count) {
        int index = itemExists(id);
        Boolean ret = false;

        if (index != -1) {
            cart.get(index).setCount(count);
            ret = true;
        }

        return ret;
    }
}
