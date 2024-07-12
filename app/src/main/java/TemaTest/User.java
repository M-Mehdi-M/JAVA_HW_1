package TemaTest;

import java.io.*;

public class User {
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username) {
        this.username = username;
    }

    public int checkIfUserExist() { // check if user already exists
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(this.username)) {
                    num = 1;
                    String passw = userInfo[1];
                    int end = passw.indexOf("<");
                    if (passw.substring(0, end).equals(this.password)) {
                        num = 11;
                        break;
                    }
                    break;
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        return num;
    }

    public void addUsername() { // add users information
        try (FileWriter fw = new FileWriter("dataBase.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter out = new PrintWriter(bw)) {
            out.println(username + "," + password + "<>[]");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void addFollowing(String user1, String user2) { // update dataBase file and add user1 following user2
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(user1)) {
                    int num = line.indexOf(">");
                    String newLine = line.substring(0,num) + user2 + "!>" + line.substring(num + 1);
                    line = newLine;
                }
                sb.append(line).append("\n");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter("dataBase.txt"))) {
                pw.print(sb.toString());
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void addFollowers(String user1, String user2) { // update dataBase file and add user2 is follower of user1
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(user2)) {
                    int num = line.indexOf("]");
                    String newLine = line.substring(0,num) + user1 + "@]" + line.substring(num + 1);
                    line = newLine;
                }
                sb.append(line).append("\n");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter("dataBase.txt"))) {
                pw.print(sb.toString());
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public int checkIfAlreadyFollowing(String user1, String user2) { // check if already user1 following user2
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(user1)) {
                    if (line.contains(user2)) {
                        num = 1;
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        return num;
    }

    public void removeFollowing(String user1, String user2) { // update dataBase file user1 unfollowing user2
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(user1)) {
                    int first = line.indexOf("<");
                    int last = line.indexOf(">");
                    String range = line.substring(first + 1, last);
                    if (range.contains("!")) {
                        String[] list = range.split("!");
                        String[] newSubString = new String[list.length-1];
                        for(int i=0, k=0;i<list.length;i++){
                            if(!(list[i].equals(user2))){
                                newSubString[k]=list[i];
                                k++;
                            }
                        }
                        String newLine = line.substring(0,first) + "<";
                        for (int i=0;i<newSubString.length;i++) {
                            if (i == 0) {
                                newLine += newSubString[i] + "!";
                            } else  {
                                newLine += newSubString[i];
                            }
                        }
                        newLine += ">" + line.substring(last + 1);
                        line = newLine;
                    } else {
                        return;
                    }
                }
                sb.append(line).append("\n");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter("dataBase.txt"))) {
                pw.print(sb.toString());
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void removeFollowers(String user1, String user2) { // update dataBase file user1 is not follower of user2 anymore
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(user2)) {
                    int first = line.indexOf("[");
                    int last = line.indexOf("]");
                    String range = line.substring(first + 1, last);
                    if (range.contains("@")) {
                        String[] list = range.split("@");
                        String[] newSubString = new String[list.length-1];
                        for(int i=0, k=0;i<list.length;i++){
                            if(!(list[i].equals(user1))){
                                newSubString[k]=list[i];
                                k++;
                            }
                        }
                        String newLine = line.substring(0,first) + "[";
                        for (int i=0;i<newSubString.length;i++) {
                            if (i == 0) {
                                newLine += newSubString[i] + "@";
                            } else  {
                                newLine += newSubString[i];
                            }
                        }
                        newLine += "]" + line.substring(last + 1);
                        line = newLine;
                    } else {
                        return;
                    }
                }
                sb.append(line).append("\n");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter("dataBase.txt"))) {
                pw.print(sb.toString());
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void getFollowingList(String username) { // get a list of users who are following by username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String followingList = "";
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(username)) {
                    int start = line.indexOf("<") + 1;
                    int end = line.indexOf(">");
                    followingList = line.substring(start, end);
                }
            }
            String[] following = followingList.split("!");
            System.out.print("{ 'status' : 'ok', 'message' : [ ");
            for (int i = 0; i < following.length; i++) {
                if (i == following.length - 1) {
                    System.out.print(following[i] + " ]}");
                } else {
                    System.out.print(following[i] + ", ");
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void getFollowersList(String username) { // get a list of users who are followers of username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String followingList = "";
            while ((line = br.readLine()) != null) {
                String[] userInfo = line.split(",");
                if (userInfo[0].equals(username)) {
                    int start = line.indexOf("[") + 1;
                    int end = line.indexOf("]");
                    followingList = line.substring(start, end);
                }
            }
            String[] following = followingList.split("@");
            System.out.print("{ 'status' : 'ok', 'message' : [ ");
            for (int i = 0; i < following.length; i++) {
                if (i == following.length - 1) {
                    System.out.print(following[i] + " ]}");
                } else {
                    System.out.print(following[i] + ", ");
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void getMostFollowed() { // displays 5 most followed users
        String[] mostFollowed = null;
        int i = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line1;
            while ((line1 = br.readLine()) != null) {
                if (line1.contains("]") && line1.contains("@")) {
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String explore = "";
            String username = "";
            mostFollowed = new String[i];
            i = 0;
            while ((line = br.readLine()) != null) {
                if (line.contains("]") && line.contains("@")) {
                    int start = line.indexOf("[") + 1;
                    int end = line.indexOf("]");
                    explore = line.substring(start, end);
                    int num = line.indexOf(",");
                    username = line.substring(0,num);
                    String[] postList = explore.split("@");
                    int number = postList.length;
                    mostFollowed[i] = "{'username' : "+username+",'number_of_followers' : ' "+ number +"' }";
                    i++;
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        sortMaxMin(mostFollowed);
        System.out.print("{ 'status' : 'ok', 'message' : [");
        for (int j = 0; j<mostFollowed.length && j < 5; j++) {
            if (j == mostFollowed.length-1) {
                System.out.print(mostFollowed[j]);
            } else {
                System.out.print(mostFollowed[j] + ",");
            }
        }
        System.out.print(" ]}");
    }

    public int getFollowednum(String text) { // returns the number of followers
        int num = text.indexOf("'number_of_followers' : ' ");
        num += "'number_of_followers' : ' ".length();
        int numFollowers = Integer.parseInt(text.substring(num, num+1));
        return numFollowers;
    }

    public void sortMaxMin(String[] array) { // sort array after followers number
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int num1 = getFollowednum(array[j]);
                int num2 = getFollowednum(array[j + 1]);
                if (num1 < num2) {
                    String temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}
