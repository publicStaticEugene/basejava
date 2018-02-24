import java.util.Arrays;

/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];
    private int size = 0;

    void clear() {
        storage = new Resume[10_000];
        size = 0;
    }

    void save(Resume r) {
        if (size == storage.length) {
            System.out.println("There are no free memory");
            return;
        } else if (r.uuid == null) {
            return;
        }
        storage[size] = r;
        size++;
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid))
                return storage[i];
        }
        return null;
    }

    void delete(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                storage[i] = null;
                index = i;
            }
        }
        if (index == -1) {
            System.out.println("There are no element to delete");
            return;
        } else if (index < storage.length - 1) {
            for (; index < size; index++) {
                if (index < storage.length - 1) {
                    storage[index] = storage[index + 1];
                } else {
                    storage[index] = null;
                }
            }
        }
        size--;
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        return Arrays.copyOfRange(storage, 0, size);
    }

    int size() {
        return size;
    }
}
