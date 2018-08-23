package darzhain.merge;

import java.io.*;
import java.util.ArrayDeque;
import java.util.Queue;

public class MergeSort {
    final private int SIZE = 100000000;

    private int countTempFiles = 0;
    private Queue<File> fileQueue;

    private boolean isAscending;
    private boolean isIntegers;

    MergeSort(boolean isAscending, boolean isIntegers) {
        fileQueue = new ArrayDeque<>();
        this.isAscending = isAscending;
        this.isIntegers = isIntegers;
    }

    private void merge(int arr[], int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int L[] = new int [n1];
        int R[] = new int [n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (isAscending) {
                if (L[i] <= R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            } else {
                if (L[i] > R[j]) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private void mergeStrings(String[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        String[] L = new String[n1];
        String[] R = new String[n2];

        for (int i = 0; i < n1; ++i)
            L[i] = arr[l + i];
        for (int j = 0; j < n2; ++j)
            R[j] = arr[m + 1 + j];

        int i = 0, j = 0;
        int k = l;
        while (i < n1 && j < n2) {
            if (isAscending) {
                if (R[j].compareTo(L[i]) >= 0) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            } else {
                if (L[i].compareTo(R[j]) >= 0) {
                    arr[k] = L[i];
                    i++;
                } else {
                    arr[k] = R[j];
                    j++;
                }
            }
            k++;
        }

        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
        }

        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
        }
    }

    private void mergeSort(int arr[], int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSort(arr, l, m);
            mergeSort(arr , m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private void mergeSortStrings(String[] arr, int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergeSortStrings(arr, l, m);
            mergeSortStrings(arr , m + 1, r);
            mergeStrings(arr, l, m, r);
        }
    }

    private void writeSortedSubArray(int[] array, int length, File file)
            throws FileNotFoundException {
        System.out.println("Write to File");
        PrintWriter printWriter = new PrintWriter(file);
        for (int i = 0; i < length; i++) {
            printWriter.println(array[i]);
        }
        printWriter.close();
    }

    private void writeSortedSubArray(String[] array, int length, File file)
            throws FileNotFoundException {
        System.out.println("Write to File");
        PrintWriter printWriter = new PrintWriter(file);
        for (int i = 0; i < length; i++) {
            printWriter.println(array[i]);
        }
        printWriter.close();
    }

    private File createTempFileName(File file) {
        return new File(file.getName() + "_" + countTempFiles++);
    }

    public void mergeSortFile(File file) throws FileNotFoundException, IOException {
        if (isIntegers) {
            forIntegers(file);
        } else {
            forStrings(file);
        }

        System.out.println("Sub files complete");

        File resultFile = null;
        String filename = file.getName();
        for (int j = 0; fileQueue.peek() != null; j++) {
            File f1 = fileQueue.poll();
            resultFile = f1;
            File f2;
            if (fileQueue.peek() != null) {
                f2 = fileQueue.poll();
                resultFile = new File(filename + "_" + j);
                mergeFiles(f1, f2, resultFile, true);
                fileQueue.offer(resultFile);
            } else
                break;
        }
        file.delete();
        if (resultFile != null)
            resultFile.renameTo(new File(filename));

    }

    private void forIntegers(File file) throws FileNotFoundException, IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        int[] array = new int[SIZE];

        String s;
        int num;
        int i;
        for (i = 0; (s = reader.readLine()) != null; i++) {
            if (i == SIZE) {
                mergeSort(array, 0, i - 1);
                File tmpFile = createTempFileName(file);
                writeSortedSubArray(array, i, tmpFile);
                fileQueue.offer(tmpFile);
                i = 0;
            }
            if (Main.isNumber(s)) {
                num = Integer.parseInt(s);
                array[i] = num;
            }
        }
        if (i != 0) {
            mergeSort(array, 0, i - 1);
            File tmpFile = createTempFileName(file);
            writeSortedSubArray(array, i, tmpFile);
            fileQueue.offer(tmpFile);
        }
        reader.close();
    }

    private void forStrings(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String[] array = new String[SIZE];

        String s;
        int i;
        for (i = 0; (s = reader.readLine()) != null; i++) {
            if (i == SIZE) {
                mergeSortStrings(array, 0, i - 1);
                File tmpFile = createTempFileName(file);
                writeSortedSubArray(array, i, tmpFile);
                fileQueue.offer(tmpFile);
                i = 0;
            }
            array[i] = s;
        }
        if (i != 0) {
            mergeSortStrings(array, 0, i - 1);
            File tmpFile = createTempFileName(file);
            writeSortedSubArray(array, i, tmpFile);
            fileQueue.offer(tmpFile);
        }
        reader.close();
    }

    public void mergeFiles(File file1, File file2, File file, boolean isNeedDelete)
            throws FileNotFoundException, IOException {
        File tmpFile = null;
        if (file1.getName().equals(file.getName())) {
            tmpFile = new File(file.getName() + "$");
        }

        File resultFile;
        if (tmpFile == null)
            resultFile = file;
        else
            resultFile = tmpFile;

        BufferedReader reader1 = new BufferedReader(new FileReader(file1));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2));
        PrintWriter printWriter = new PrintWriter(resultFile);

        String s1 = reader1.readLine();
        String s2 = reader2.readLine();
        int num1;
        int num2;
        if (isIntegers) {
            while ((s1 != null) && (s2 != null)) {
                num1 = Integer.parseInt(s1);
                num2 = Integer.parseInt(s2);
                if (isAscending) {
                    if (num1 < num2) {
                        printWriter.println(num1);
                        s1 = reader1.readLine();
                    } else {
                        printWriter.println(num2);
                        s2 = reader2.readLine();
                    }
                } else {
                    if (num1 > num2) {
                        printWriter.println(num1);
                        s1 = reader1.readLine();
                    } else {
                        printWriter.println(num2);
                        s2 = reader2.readLine();
                    }
                }
            }
        } else {
            while ((s1 != null) && (s2 != null)) {
                if (isAscending) {
                    if (s2.compareTo(s1) > 0) {
                        printWriter.println(s1);
                        s1 = reader1.readLine();
                    } else {
                        printWriter.println(s2);
                        s2 = reader2.readLine();
                    }
                } else {
                    if (s1.compareTo(s2) > 0) {
                        printWriter.println(s1);
                        s1 = reader1.readLine();
                    } else {
                        printWriter.println(s2);
                        s2 = reader2.readLine();
                    }
                }
            }
        }

        if (s1 != null) {
            printWriter.println(s1);
            while ((s1 = reader1.readLine()) != null) {
                printWriter.println(s1);
            }
        }

        if (s2 != null) {
            printWriter.println(s2);
            while ((s2 = reader2.readLine()) != null) {
                printWriter.println(s2);
            }
        }

        //*****закрытие файлов и переименование результирующего файла************
        String name = file1.getName();
        reader1.close();
        reader2.close();
        printWriter.close();
        if (isNeedDelete) {
            file1.delete();
            file2.delete();
        }
        if (tmpFile != null) {
            resultFile.renameTo(new File(name));
        }
    }
}
