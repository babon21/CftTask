package darzhain.merge;

import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length < 3)
                throw new UserException("Некорректные параметры");

            boolean isFindType = false;
            boolean isFindRule = false;
            File outputFile = null;
            Deque<File> inputFiles = new ArrayDeque<>();
            int countInputFiles = 0;
            boolean isAscending = true;
            boolean isIntegers = true;
            for (int i = 0; i < args.length; i++) {
                if (args[i].equals("-s")) {
                    if (isFindType)
                        throw new UserException("Уже введен тип данных");
                    else {
                        isIntegers = false;
                        isFindType = true;
                    }
                } else if (args[i].equals("-i")) {
                    if (isFindType)
                        throw new UserException("Уже введен тип данных");
                    else {
                        isIntegers = true;
                        isFindType = true;
                    }
                } else if (args[i].equals("-a") && !isFindRule) {
                    isFindRule = true;
                    isAscending = true;
                } else if (args[i].equals("-d") && !isFindRule) {
                    isFindRule = true;
                    isAscending = false;
                } else if (outputFile == null) {
                    outputFile = new File(args[i]);
                } else {
                    inputFiles.offer(new File(args[i]));
                    countInputFiles++;
                }
            }
            if (!isFindType)
                throw new UserException("Тип данных не введен");
            for (File file : inputFiles)
                if (!file.exists())
                    throw new UserException(file + ", такого файла не существует. " +
                    "Запустите программу снова, с параметрами такими, что входные файлы " +
                    "существовали");

            MergeSort mergeSort = new MergeSort(isAscending, isIntegers);
            for (int i = 0; i < countInputFiles; i++) {
                File file = inputFiles.poll();
                mergeSort.mergeSortFile(file);
                inputFiles.offer(file);
            }

            while (inputFiles.peek() != null) {
                File f1 = inputFiles.poll();
                File f2;
                if (inputFiles.peek() != null) {
                    f2 = inputFiles.poll();
                    mergeSort.mergeFiles(f1, f2, outputFile, false);
                    inputFiles.addFirst(outputFile);
                } else
                    break;
            }
        } catch (UserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException | NullPointerException e1) {
            return false;
        }
        return true;
    }
}

