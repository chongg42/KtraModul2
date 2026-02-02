import java.util.*;

public class Repository<T> {
    protected Map<String, T> storage;

    public Repository() {
        this.storage = new HashMap<>();
    }

    // Thêm phần tử
    public void add(String id, T item) {
        storage.put(id, item);
    }

    // Xóa phần tử
    public void remove(String id) {
        storage.remove(id);
    }

    // Tìm theo ID - trả về Optional
    public Optional<T> findById(String id) {
        return Optional.ofNullable(storage.get(id));
    }

    // Lấy toàn bộ
    public List<T> findAll() {
        return new ArrayList<>(storage.values());
    }

    // Kiểm tra tồn tại
    public boolean exists(String id) {
        return storage.containsKey(id);
    }

    // Lấy số lượng
    public int size() {
        return storage.size();
    }

    // Xóa toàn bộ
    public void clear() {
        storage.clear();
    }
}
