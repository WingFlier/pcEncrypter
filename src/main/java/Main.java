import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

// TODO 2 features
// TODO 1) encrypt file
// TODO 2) encrypt text

class Main
{
    public static void main(String[] args)
    {
        System.out.println("Welcome. type your command");
        System.out.println("type -help for command list");

        waitForCommand();
    }

    private static void waitForCommand() throws RuntimeException
    {
        try
        {
            Scanner scanner = new Scanner(System.in);
            String s = scanner.nextLine();
            if (s.equals("-help"))
            {
                System.out.println("-file -e/-d -path -key -vector");
                System.out.println("-text -e/-d -text to encrypt -key -vector //note key and vector must be 16 length");
            } else if (s.startsWith("-file"))
            {
                /**
                 * split[2] - encrypt or decrypt
                 * split[3] - file path
                 * split[4] - key
                 * split[5] - vector
                 * */
                String[] split = s.split("-");
                if (split.length <= 2)
                    throw new RuntimeException(ErrorConstants.MISSING_ARGUMENTS);

                File file = new File(split[3].trim());
                if (!file.isFile())
                    throw new FileNotFoundException();
                parseFile(file, split);
            } else if (s.startsWith("-text"))
            {
                /**
                 * split[2] - encrypt or decrypt
                 * split[3] - text to encrypt
                 * split[4] - key
                 * split[5] - vector
                 * */
                String[] split = s.split("-");
                if (split.length <= 2)
                    throw new RuntimeException(ErrorConstants.MISSING_ARGUMENTS);
                if (split[4].length() < 16 || split[5].length() < 16)
                    throw new RuntimeException(ErrorConstants.KEY_LENGTH_ERROR);
                if (split[2].trim().equals("e"))
                    System.out.println(Encryptor.encrypt(split[4].trim(), split[5].trim(), split[3].trim()));
                else if (split[2].trim().equals("d"))
                    System.out.println(Encryptor.decrypt(split[4].trim(), split[5].trim(), split[3].trim()));
                else throw new RuntimeException(ErrorConstants.MISSING_ARGUMENTS);
            } else
                throw new RuntimeException(ErrorConstants.MISSING_ARGUMENTS);

            waitForCommand();
        } catch (RuntimeException e)
        {
            if (e.getMessage().equals(ErrorConstants.MISSING_ARGUMENTS))
            {
                System.out.println("some argument are missing");
            } else if (e.getMessage().equals(ErrorConstants.KEY_LENGTH_ERROR))
            {
                System.out.println("check key or vector length");
            }
        } catch (FileNotFoundException e)
        {
            System.out.println("error with finding the file");
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            waitForCommand();
        }
    }

    private static void parseFile(File file, String[] split) throws IOException
    {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        StringBuilder builder = new StringBuilder();
        while ((line = reader.readLine()) != null)
        {
            builder.append(line);
        }
        if (split[4].length() < 16 || split[5].length() < 16)
            throw new RuntimeException(ErrorConstants.KEY_LENGTH_ERROR);
        if (split[2].trim().equals("e"))
            System.out.println(Encryptor.encrypt(split[4].trim(), split[5].trim(), builder.toString()));
        else if (split[2].trim().equals("d"))
            System.out.println(Encryptor.decrypt(split[4].trim(), split[5].trim(), builder.toString()));
        else throw new RuntimeException(ErrorConstants.MISSING_ARGUMENTS);
    }
}