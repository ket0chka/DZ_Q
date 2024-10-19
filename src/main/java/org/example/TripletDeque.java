package org.example;

import java.util.*;

public class TripletDeque<T> implements Deque<T>, Containerable {
    private static final int how_many_elements_incomtainer = 5;
    private static final int amount_of_queue = 1000;

    private int the_container_size;
    private int maxCapacity;

    private int size;

    private Container<T> first;
    private Container<T> last;

    // Конструктор с заданным размером контейнера
    public TripletDeque(int the_container_size) {
        this(the_container_size, amount_of_queue);
    }
    // Конструктор по умолчанию
    public TripletDeque() {
        this(how_many_elements_incomtainer, amount_of_queue);
    }

    public TripletDeque(int the_container_size, int maxCapacity) {
        this.the_container_size = the_container_size;
        this.maxCapacity = maxCapacity;
        this.first = new Container<>(the_container_size);
        this.last = first;
    }
    // Метод для получения контейнера по индексу
    @Override
    public Object[] getContainerByIndex(int cIndex) {
        Container<T> these = first;
        int index = 0;
        // Проход по контейнерам до тех пор, пока не найдем нужный индекс
        while (these != null) {
            if (index == cIndex) {
                return these.elements;
            }
            these = these.next;
            index++;
        }
        return null;
    }

    private static class Container<T> {
        T[] elements;
        Container<T> next;
        Container<T> previous;

        Container(int size) {
            this.elements = (T[]) new Object[size];
        }
    }

    @Override
    public void addFirst(T t) {
        if (size >= maxCapacity) {
            throw new IllegalStateException("Заполнен");
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (isContainerFull(first)) {
            Container<T> newContainer = new Container<>(the_container_size);
            newContainer.next = first;
            first.previous = newContainer;
            first = newContainer;
        }
        // Сдвиг элементов в первом контейнере для освобождения места
        for (int i = the_container_size - 1; i > 0; i--) {
            first.elements[i] = first.elements[i - 1];
        }
        first.elements[0] = t;
        size++;
    }
    // Метод для добавления элемента в конец очереди
    @Override
    public void addLast(T t) {
        if (size >= maxCapacity) {
            throw new IllegalStateException("Очередь заполнена");
        }
        if (t == null) {
            throw new NullPointerException("Добавление пустого элемента");
        }
        if (isContainerFull(last)) {
            Container<T> newContainer = new Container<>(the_container_size);
            newContainer.previous = last;
            last.next = newContainer;
            last = newContainer;
        }
        // Добавление элемента в первую свободную ячейку последнего контейнера
        for (int i = 0; i < the_container_size; i++) {
            if (last.elements[i] == null) {
                last.elements[i] = t;
                size++;
                return;
            }
        }
    }
    // Метод для проверки, заполнен ли контейнер
    private boolean isContainerFull(Container<T> container) {
        for (int i = 0; i < the_container_size; i++) {
            if (container.elements[i] == null) {
                return false;
            }
        }
        return true;
    }
    // Метод для попытки добавления элемента в начало очереди
    @Override
    public boolean offerFirst(T t) {
        if (size >= maxCapacity) {
            return false;
        }
        addFirst(t);
        return true;
    }
    // Метод для попытки добавления элемента в конец очереди
    @Override
    public boolean offerLast(T t) {
        if (size >= maxCapacity) {
            return false;
        }
        addLast(t);
        return true;
    }
    // Метод для удаления первого элемента из очереди
    @Override
    public T removeFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        T element = first.elements[0];
        // Сдвиг элементов в первом контейнере для удаления первого элемента
        for (int i = 0; i < the_container_size - 1; i++) {
            first.elements[i] = first.elements[i + 1];
        }
        first.elements[the_container_size - 1] = null;
        size--;
        // Если первый контейнер стал пустым и есть следующий контейнер, перемещаем указатель
        if (first.elements[0] == null && first.next != null) {
            first = first.next;
            first.previous = null;
        }
        return element;
    }
    // Метод для удаления последнего элемента из очереди
    @Override
    public T removeLast() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        int lastIndex = the_container_size - 1;
        // Поиск последнего непустого элемента в последнем контейнере
        while (lastIndex >= 0 && last.elements[lastIndex] == null) {
            lastIndex--;
        }
        if (lastIndex >= 0) {
            T element = last.elements[lastIndex];
            last.elements[lastIndex] = null;
            size--;
            // Если последний контейнер стал пустым и есть предыдущий контейнер, перемещаем указатель
            if (lastIndex == 0 && last.previous != null) {
                last = last.previous;
                last.next = null;
            }
            return element;
        }
        // Если последний контейнер пуст и есть предыдущий контейнер, перемещаем указатель и повторяем
        if (last.previous != null) {
            last = last.previous;
            last.next = null;
            return removeLast();
        }
        throw new NoSuchElementException("Очередь пуста");
    }
    // Метод для попытки удаления первого элемента из очереди
    @Override
    public T pollFirst() {
        if (size == 0) {
            return null;
        }
        return removeFirst();
    }
    // Метод для попытки удаления последнего элемента из очереди
    @Override
    public T pollLast() {
        if (size == 0) {
            return null;
        }
        return removeLast();
    }
    // Метод для получения первого элемента из очереди
    @Override
    public T getFirst() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        return first.elements[0];
    }
    // Метод для получения последнего элемента из очереди
    @Override
    public T getLast() {
        if (size == 0) {
            throw new NoSuchElementException("Очередь пуста");
        }
        int lastIndex = the_container_size - 1;
        // Поиск последнего непустого элемента в последнем контейнере
        while (lastIndex >= 0 && last.elements[lastIndex] == null) {
            lastIndex--;
        }
        if (lastIndex >= 0) {
            return last.elements[lastIndex];
        }
        // Если последний контейнер пуст и есть предыдущий контейнер, перемещаем указатель и повторяем
        if (last.previous != null) {
            last = last.previous;
            return getLast();
        }
        throw new NoSuchElementException("Очередь пуста");
    }
    // Метод для получения первого элемента из очереди без удаления
    @Override
    public T peekFirst() {
        if (size == 0) {
            return null;
        }
        return first.elements[0];
    }
    // Метод для получения последнего элемента из очереди без удаления
    @Override
    public T peekLast() {
        if (size == 0) {
            return null;
        }
        return last.elements[the_container_size - 1];
    }
    // Метод для удаления первого вхождения элемента в очередь
    @Override
    public boolean removeFirstOccurrence(Object o) {
        Container<T> these = first;
        while (these != null) {
            for (int i = 0; i < the_container_size; i++) {
                if (Objects.equals(o, these.elements[i])) {
                    // Сдвиг элементов для удаления найденного объекта
                    for (int j = i; j < the_container_size - 1; j++) {
                        these.elements[j] = these.elements[j + 1];
                    }
                    these.elements[the_container_size - 1] = null;
                    size--;

                    if (isContainerEmpty(these)) {
                        removeEmptyContainer(these);
                    }
                    return true;
                }
            }
            these = these.next;
        }
        return false;
    }
    // Метод для проверки, пуст ли контейнер
    private boolean isContainerEmpty(Container<T> container) {
        for (T element : container.elements) {
            if (element != null) {
                return false;
            }
        }
        return true;
    }
    // Метод для удаления пустого контейнера
    private void removeEmptyContainer(Container<T> container) {
        if (container.previous != null) {
            container.previous.next = container.next;
        } else {
            first = container.next; // Если это первый контейнер
        }
        if (container.next != null) {
            container.next.previous = container.previous;
        } else {
            last = container.previous; // Если это последний контейнер
        }
    }
    // Метод для удаления последнего вхождения элемента в очередь
    @Override
    public boolean removeLastOccurrence(Object o) {
            Container<T> these = first;
            while (these != null) {
                for (int i = 0; i < the_container_size; i++) {
                    if (Objects.equals(o, these.elements[i])) {
                        // Сдвиг элементов для удаления найденного объекта
                        for (int j = i; j < the_container_size - 1; j++) {
                            these.elements[j] = these.elements[j + 1];
                        }
                        these.elements[the_container_size - 1] = null; // Обнуление последнего элемента
                        size--;

                        // Проверка на пустоту контейнера и его удаление
                        if (isContainerEmpty(these)) {
                            removeEmptyContainer(these);
                        }
                        return true;
                    }
                }
                these = these.next;
            }
            return false;
    }
    // Метод для добавления элемента в конец очереди
    @Override
    public boolean add(T t) {
        addLast(t);
        return true;
    }
    // Метод для попытки добавления элемента в конец очереди
    @Override
    public boolean offer(T t) {
        return offerLast(t);
    }
    // Метод для удаления первого элемента из очереди
    @Override
    public T remove() {
        return removeFirst();
    }
    // Метод для попытки удаления первого элемента из очереди
    @Override
    public T poll() {
        return pollFirst();
    }
    // Метод для получения первого элемента из очереди
    @Override
    public T element() {
        return getFirst();
    }
    // Метод для получения первого элемента из очереди без удаления
    @Override
    public T peek() {
        return peekFirst();
    }
    // Метод для добавления всех элементов из коллекции в конец очереди
    @Override
    public boolean addAll(Collection<? extends T> c) {
        for (T t : c) {
            addLast(t);
        }
        return true;
    }
    // Метод для удаления всех элементов, присутствующих в коллекции
    @Override
    public boolean removeAll(Collection<?> c) {
//      отслеживаниe того, были ли какие-либо изменения в коллекции.
        boolean modified = false;
        for (Object o : c) {
            while (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }
    // Метод для удаления всех элементов, отсутствующих в коллекции
    @Override
    public boolean retainAll(Collection<?> c) {
        boolean modified = false;
        Iterator<T> iterator = iterator();
        while (iterator.hasNext()) {
            if (!c.contains(iterator.next())) {
                iterator.remove();
                modified = true;
            }
        }
        return modified;
    }
    // Метод для очистки очереди
    @Override
    public void clear() {
        first = new Container<>(the_container_size);
        last = first;
        size = 0;
    }
    // Метод для добавления элемента в начало очереди
    @Override
    public void push(T t) {
        addFirst(t);
    }

    @Override
    public T pop() {
        return removeFirst();
    }
    // Метод для удаления первого вхождения указанного объекта из очереди
    @Override
    public boolean remove(Object o) {
        Container<T> these = first;
        while (these != null) {
            for (int i = 0; i < the_container_size; i++) {
                if (Objects.equals(o, these.elements[i])) {
                    // Сдвиг элементов для удаления найденного объекта
                    for (int j = i; j < the_container_size - 1; j++) {
                        these.elements[j] = these.elements[j + 1];
                    }
                    these.elements[the_container_size - 1] = null; // Обнуление последнего элемента
                    size--;
                    // Проверка на пустоту контейнера и его удаление
                    if (isContainerEmpty(these)) {
                        removeEmptyContainer(these);
                    }
                    return true;
                }
            }
            these = these.next;
        }
        return false;
    }
    // Метод для проверки, содержатся ли все элементы коллекции в очереди
    @Override
    public boolean containsAll(Collection<?> c) {
        for (Object o : c) {
            if (!contains(o)) {
                return false;
            }
        }
        return true;
    }
    // Метод для проверки, содержится ли указанный объект в очереди
    @Override
    public boolean contains(Object o) {
        Container<T> current = first;
        while (current != null) {
            for (int i = 0; i < the_container_size; i++) {
                if (Objects.equals(o, current.elements[i])) {
                    return true;
                }
            }
            current = current.next;
        }
        return false;
    }
    // Метод для получения текущего размера очереди
    @Override
    public int size() {
        return size;
    }
    // Метод для проверки, пуста ли очередь
    @Override
    public boolean isEmpty() {
        return size == 0;
    }
    // Метод для получения итератора для перебора элементов очереди
    @Override
    public Iterator<T> iterator() {
        return new TripletDequeIterator();
    }
// Класс, реализующий итератор для перебора элементов очереди
    private class TripletDequeIterator implements Iterator<T> {
        private Container<T> theseContainer = first;
        private int currentIndex = 0;
    // Метод для проверки наличия следующего элемента
        @Override
        public boolean hasNext() {
            while (theseContainer != null) {
                if (currentIndex < the_container_size && theseContainer.elements[currentIndex] != null) {
                    return true;
                }
                theseContainer = theseContainer.next;
                currentIndex = 0;
            }
            return false;
        }
    // Метод для получения следующего элемента
        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = theseContainer.elements[currentIndex];
            currentIndex++;
            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
    // Метод для преобразования очереди в массив объектов
    @Override
    public Object[] toArray() {
        Object[] array = new Object[size];
        int index = 0;
        Container<T> current = first;
        while (current != null) {
            for (int i = 0; i < the_container_size; i++) {
                if (current.elements[i] != null) {
                    array[index++] = current.elements[i];
                }
            }
            current = current.next;
        }
        return array;
    }
    // Метод для преобразования очереди в массив указанного типа
    @Override
    public <T1> T1[] toArray(T1[] a) {
        if (a.length < size) {
//            создает новый массив типа T1[] с размером size, используя рефлексию для определения типа элементов массива a.
            a = (T1[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        int index = 0;
        Container<T> current = first;
        while (current != null) {
            for (int i = 0; i < the_container_size; i++) {
                if (current.elements[i] != null) {
                    a[index++] = (T1) current.elements[i];
                }
            }
            current = current.next;
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }
    // Метод для получения итератора для перебора элементов очереди в обратном порядке
    @Override
    public Iterator<T> descendingIterator() {
        return new DescendingTripletDequeIterator();
    }
// Класс, реализующий итератор для перебора элементов очереди в обратном порядке
    private class DescendingTripletDequeIterator implements Iterator<T> {
        private Container<T> theseContainer = last;
        private int currentIndex = the_container_size - 1;
    // Метод для проверки наличия следующего элемента
        @Override
        public boolean hasNext() {
            while (theseContainer != null) {
                if (currentIndex >= 0 && theseContainer.elements[currentIndex] != null) {
                    return true;
                }
                theseContainer = theseContainer.previous;
                currentIndex = the_container_size - 1;
            }
            return false;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T element = theseContainer.elements[currentIndex];
            currentIndex--;
            return element;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        }
    }
