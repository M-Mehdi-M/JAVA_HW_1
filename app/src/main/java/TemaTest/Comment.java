package TemaTest;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Comment implements Likeable {
    private String comment;

    public Comment(String comment) {
        this.comment = comment;
    }

    public Comment() {}

    public void addComment(String username, String postId) { // add a comment by username for post postId
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String currentDateAsString = dateFormat.format(date);
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        int j = Integer.parseInt(postId.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            if (i == j) {
                                int num = list[i].indexOf(")");
                                String newLine1 = list[i].substring(0,num) + username + "%" + currentDateAsString + "&" +
                                        "*" + this.comment + "#)" + list[i].substring(num + 1);
                                list[i] = newLine1;
                            }
                        }

                        String newLine = line.substring(0,first) + "{";
                        for (int i=0;i<list.length;i++) {
                            newLine += list[i] + "~";
                        }
                        newLine += "}" + line.substring(last + 1);
                        line = newLine;
                    } else {
                        break;
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

    public int checkIfCommentExists(String username) { // check if username wrote comment for post
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("{") && line.contains(username+"%")) {
                    num = 1;
                    break;
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        return num;
    }

    public int ifCommentExists(String commentId) { // check if comment commentId exists
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String[] postList;
            int j = Integer.parseInt(commentId.replace("'", "")) - 1;
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    postList = line.split("~");
                    for (int i = 0; i<postList.length; i++) {
                        if (i == j && postList[i].contains("#")) {
                            num = 1;
                            break;
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        return num;
    }

    public void deleteComment(String username, String id) { // delete written by username using id
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            String newLine = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        int j = Integer.parseInt(id.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            if (list[i].contains(username+"%")){
                                int first1 = line.indexOf("(");
                                int last1 = line.indexOf(")");
                                String range1 = line.substring(first1 + 1, last1);
                                if (range1.contains("#")) {
                                    String[] list1 = range1.split("#");
                                    String[] newSubstring1 = new String[list1.length-1];
                                    for (int m=0, n=0; m<list1.length; m++) {
                                        if (m!=j) {
                                            newSubstring1[n]=list1[m];
                                            n++;
                                        }
                                    }
                                    newLine += line.substring(0,first1 + 1);
                                    for (int u = 0; u<newSubstring1.length;u++) {
                                        newLine += newSubstring1[u] + "#";
                                    }
                                    newLine += line.substring(last1);
                                } else {
                                    break;
                                }
                            }
                        }
                        line = newLine;
                    } else {
                        break;
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

    @Override
    public void addLike(String username, String id) { // add like to comment id by username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        String newLine1 = "";
                        String newLine2 = "";
                        int j = Integer.parseInt(id.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            int num = list[i].indexOf("(");
                            int num2 = list[i].indexOf(")");
                            if (list[i].contains("#")) {
                                String subLine = line.substring(num+2,num2+1);
                                String[] subsub = subLine.split("#");
                                for (int k=0;k<subsub.length;k++) {
                                    if (k == j) {
                                        int num3=subsub[k].indexOf("&");
                                        int num4 = subsub[k].indexOf("*");
                                        if ((subsub[k].substring(num3+1,num4).contains(username))) {
                                            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier"+
                                                    " to like was not valid'}");
                                            return;
                                        }
                                        String subsubsub = subsub[k].substring(num3+1,num4);
                                        if (subsubsub.contains("<")) {
                                            subsubsub += username + "<";
                                            newLine1 = subsub[k].substring(0,num3+1) + subsubsub + subsub[k].substring(num4);
                                        } else {
                                            newLine1 = subsub[k].substring(0,num3+1) + username +"<" + subsub[k].substring(num4);
                                        }
                                    }
                                }
                                newLine1 += "#";
                                newLine2 = list[i].substring(0,num+1) + newLine1 + list[i].substring(num2);
                                list[i] = newLine2;
                            }

                        }
                        String newLine = line.substring(0,first) + "{";
                        for (int i=0;i<list.length;i++) {
                            newLine += list[i] + "~";
                        }
                        newLine += "}" + line.substring(last + 1);
                        line = newLine;

                    } else {
                        break;
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
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    @Override
    public void unlike(String username, String id) { // unlike comment id by username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        String newLine1 = "";
                        String newLine2 = "";
                        int j = Integer.parseInt(id.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            int num = list[i].indexOf("(");
                            int num2 = list[i].indexOf(")");
                            if (list[i].contains("#")) {
                                String subLine = line.substring(num+2,num2+1);
                                String[] subsub = subLine.split("#");
                                for (int k=0;k<subsub.length;k++) {
                                    if (k == j) {
                                        int num3=subsub[k].indexOf("&");
                                        int num4 = subsub[k].indexOf("*");
                                        if (!(subsub[k].substring(num3+1,num4).contains(username))) {
                                            System.out.println("{ 'status' : 'error', 'message' : 'The comment identifier"+
                                                    " to unlike was not valid'}");
                                            return;
                                        }
                                        String subsubsub = subsub[k].substring(num3+1,num4);
                                        String[] nline = subsubsub.split("<");
                                        String newcommnetLike = "";
                                        if (nline.length == 1) {
                                            newLine1 = subsub[k].substring(0,num3+1) + subsub[k].substring(num4);
                                        } else {
                                            for(int m=0;m<nline.length;m++){
                                                if(!(nline[m].equals(username))){
                                                    newcommnetLike += nline[m]+"<";
                                                }
                                            }
                                            newLine1 = subsub[k].substring(0,num3+1)+ newcommnetLike + subsub[k].substring(num4);
                                        }
                                    }
                                }
                                newLine1 += "#";
                                newLine2 = list[i].substring(0,num+1) + newLine1 + list[i].substring(num2);
                                list[i] = newLine2;
                            }
                        }
                        String newLine = line.substring(0,first) + "{";
                        for (int i=0;i<list.length;i++) {
                            newLine += list[i] + "~";
                        }
                        newLine += "}" + line.substring(last + 1);
                        line = newLine;
                    } else {
                        break;
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
        System.out.println("{ 'status' : 'ok', 'message' : 'Operation executed successfully'}");
    }

    public void getMostCommentedPosts() { // displays most liked posts
        String[] mostcommented = null;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String explore = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    int start = line.indexOf("{") + 1;
                    int end = line.indexOf("}");
                    explore = line.substring(start, end);
                }
            }
            String[] postList = explore.split("~");
            mostcommented = new String[postList.length];
            for (int i = 0; i < postList.length; i++) {
                int postId = i+1;
                int start = postList[i].indexOf("^");
                int end = postList[i].indexOf("(");
                int start1 = postList[i].indexOf("$");
                int end1 = postList[i].indexOf("^");
                int start2 = postList[i].indexOf("=");
                int end2 = postList[i].indexOf(")");

                String postText = postList[i].substring(start+1,end);
                String person = postList[i].substring(0,start1);
                String postDate = postList[i].substring(start2+1,end1);
                String likeList = postList[i].substring(end+1, end2);
                int numLike = 0;
                if (likeList.contains("#")) {
                    numLike = likeList.split("#").length;
                }
                mostcommented[i] = "{'post_id' : '" + postId + "'," + "'post_text' : " + postText + ", 'post_date':'" +
                        postDate + "', 'username' : " + person + ", 'number_of_comments':'" + numLike + "' }";
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        sortMaxMin(mostcommented);
        System.out.print("{ 'status' : 'ok', 'message' : [");
        for (int i = 0; i<mostcommented.length && i < 5; i++) {
            if (i == mostcommented.length-1) {
                System.out.print(mostcommented[i]);
            } else {
                System.out.print(mostcommented[i] + ",");
            }
        }
        System.out.print("]}");
    }

    public int getCommentNum(String post) { // extracts number of likes
        int num = post.indexOf("'number_of_comments':'");
        num += "'number_of_comments':'".length();
        int numComment = Integer.parseInt(post.substring(num, num+1));
        return numComment;
    }

    public void sortMaxMin(String[] array) { // sorts array base on like number
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int num1 = getCommentNum(array[j]);
                int num2 = getCommentNum(array[j + 1]);
                if (num1 < num2) {
                    String temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}
