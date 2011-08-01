import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Main {
    private static int numCounts = 10;

    interface Sort {
        void run(int[] arr);

        long reportTimeNano();

        String getName();
    }

    static class BubbleSort implements Sort {
        private long start;
        private long end;

        @Override
        public void run(int[] arr) {
            start = System.nanoTime();
            int n = arr.length;
            for (int i = 0; i < n - 1; i++) {
                for (int j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        int temp = arr[i];
                        arr[i] = arr[j];
                        arr[j] = temp;
                    }
                }
            }
            end = System.nanoTime();
        }

        @Override
        public long reportTimeNano() {
            return (end - start);
        }

        @Override
        public String getName() {
            return "Bubble Sort";
        }
    }

    static class MergeSort implements Sort {
        private long start;
        private long end;

        @Override
        public void run(int[] arr) {
            start = System.nanoTime();
            mergeSort(arr, arr.length);
            end = System.nanoTime();
        }

        @Override
        public long reportTimeNano() {
            return (end - start);
        }

        @Override
        public String getName() {
            return "Merge Sort";
        }

        private void mergeSort(int[] a, int n) {
            if (n < 2) {
                return;
            }
            int mid = n / 2;
            int[] l = new int[mid];
            int[] r = new int[n - mid];

            for (int i = 0; i < mid; i++) {
                l[i] = a[i];
            }
            for (int i = mid; i < n; i++) {
                r[i - mid] = a[i];
            }
            mergeSort(l, mid);
            mergeSort(r, n - mid);

            merge(a, l, r, mid, n - mid);
        }

        private void merge(int[] a, int[] l, int[] r, int left, int right) {

            int i = 0, j = 0, k = 0;
            while (i < left && j < right) {
                if (l[i] <= r[j]) {
                    a[k++] = l[i++];
                } else {
                    a[k++] = r[j++];
                }
            }
            while (i < left) {
                a[k++] = l[i++];
            }
            while (j < right) {
                a[k++] = r[j++];
            }
        }
    }

    static class QuickSort implements Sort {
        private long start;
        private long end;

        @Override
        public void run(int[] arr) {
            start = System.nanoTime();
            quickSort(arr, 0, arr.length - 1);
            end = System.nanoTime();
        }

        @Override
        public long reportTimeNano() {
            return (end - start);
        }

        @Override
        public String getName() {
            return "Quick Sort";
        }

        private void quickSort(int arr[], int begin, int end) {
            if (begin < end) {
                int partitionIndex = partition(arr, begin, end);

                quickSort(arr, begin, partitionIndex - 1);
                quickSort(arr, partitionIndex + 1, end);
            }
        }

        private int partition(int arr[], int begin, int end) {
            int pivot = arr[end];
            int i = (begin - 1);

            for (int j = begin; j < end; j++) {
                if (arr[j] <= pivot) {
                    i++;

                    int swapTemp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = swapTemp;
                }
            }

            int swapTemp = arr[i + 1];
            arr[i + 1] = arr[end];
            arr[end] = swapTemp;

            return i + 1;
        }

    }

    static class InsertionSort implements Sort {
        private long start;
        private long end;

        @Override
        public void run(int[] arr) {
            start = System.nanoTime();
            for (int i = 1; i < arr.length; i++) {
                int key = arr[i];
                int j = i - 1;
                while (j >= 0 && arr[j] > key) {
                    arr[j + 1] = arr[j];
                    j = j - 1;
                }
                arr[j + 1] = key;
            }
            end = System.nanoTime();
        }

        @Override
        public long reportTimeNano() {
            return (end - start);
        }

        @Override
        public String getName() {
            return "Insertion Sort";
        }
    }

    static class SelectionSort implements Sort {
        private long start;
        private long end;

        @Override
        public void run(int[] arr) {
            start = System.nanoTime();
            for (int i = 0; i < arr.length - 1; i++) {
                int minElementIndex = i;
                for (int j = i + 1; j < arr.length; j++) {
                    if (arr[minElementIndex] > arr[j]) {
                        minElementIndex = j;
                    }
                }

                if (minElementIndex != i) {
                    int temp = arr[i];
                    arr[i] = arr[minElementIndex];
                    arr[minElementIndex] = temp;
                }
            }
            end = System.nanoTime();
        }

        @Override
        public long reportTimeNano() {
            return (end - start);
        }

        @Override
        public String getName() {
            return "Selection Sort";
        }
    }

    static class Util {
        public static int[] generateRandomNumber(int n) {
            int[] output = new int[n];
            int idx = 0;
            final int low = -1000000;
            final int high = 1000000;

            Random random = new Random();
            for (int i = 0; i < n; i++) {
                output[idx++] = random.nextInt(high - low) + low;
            }
            return output;
        }

        public static void saveOutput(StringBuilder output) {
            BufferedWriter outputWriter;
            try {
                String fileName = "result/" + numCounts + "-" + System.currentTimeMillis() + ".txt";
                outputWriter = new BufferedWriter(new FileWriter(fileName));
                outputWriter.write(output.toString());
                outputWriter.flush();
                outputWriter.close();
                System.out.println("output saved: " + fileName);
            } catch (IOException e) {
                System.out.println("could not write output to the file: " + e.getMessage());
            }
        }

        public static void appendTime(Sort sort, StringBuilder output) {
            output.append(sort.getName()).append(" Duration: ").append(sort.reportTimeNano())
                    .append("ns").append(" | ").append(sort.reportTimeNano() / 1000000).append("ms\n");
        }
    }

    public static void main(String[] args) {
        Sort bubbleSort = new BubbleSort();
        Sort insertionSort = new InsertionSort();
        Sort selectionSort = new SelectionSort();
        Sort mergeSort = new MergeSort();
        Sort quickSort = new QuickSort();

        numCounts = Integer.parseInt(args[0]);

        int[] nums;
        StringBuilder output = new StringBuilder();
        output.append("Running Test for '").append(numCounts).append("' input\n");
        System.out.println("Start execution for " + numCounts + " items");

        // System.out.println("Running Bubble Sort");
        // nums = Util.generateRandomNumber(numCounts);
        // bubbleSort.run(nums);
        // Util.appendTime(bubbleSort, output);

        System.out.println("Running Merge Sort");
        nums = Util.generateRandomNumber(numCounts);
        mergeSort.run(nums);
        Util.appendTime(mergeSort, output);

        System.out.println("Running Quick Sort");
        nums = Util.generateRandomNumber(numCounts);
        quickSort.run(nums);
        Util.appendTime(quickSort, output);

        // System.out.println("Running Insertion Sort");
        // nums = Util.generateRandomNumber(numCounts);
        // insertionSort.run(nums);
        // Util.appendTime(insertionSort, output);

        // System.out.println("Running Selection Sort");
        // nums = Util.generateRandomNumber(numCounts);
        // selectionSort.run(nums);
        // Util.appendTime(selectionSort, output);

        System.out.println("^^^^^ Saving Output ^^^^^");
        Util.saveOutput(output);
        System.out.println(output);

        System.out.println("Bye!");
    }
}
