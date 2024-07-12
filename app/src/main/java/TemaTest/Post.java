package TemaTest;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Post implements Likeable {
    private String post;

    public Post(String post) {
        this.post = post;
    }

    public Post() {}

    public void addExplore() { // adding post part at the end of .txt file
        try (FileWriter fw = new FileWriter("dataBase.txt", true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println("{}");
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void addPost(String username) { // adding new post
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String lastLine = "";
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            if (!(lastLine.contains("{"))) {
                addExplore();
            }
        } catch (IOException e) {
            System.out.print(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            String currentDateAsString = dateFormat.format(date);
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int num = line.indexOf("}");
                    String newLine = line.substring(0,num) + username + "$" + "=" + currentDateAsString + "^" +
                            this.post + "()~}" + line.substring(num + 1);
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

    public void deletePost(String username, String postId) { // delete post postId wrote by username
        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(username) && line.contains("{")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        String[] newSubString = new String[list.length-1];
                        int j = Integer.parseInt(postId.replace("'", "")) - 1;
                        for(int i=0, k=0;i<list.length;i++){
                            if(i != j){
                                newSubString[k]=list[i];
                                k++;
                            }
                        }
                        String newLine = line.substring(0,first) + "{";
                        for (int i=0;i<newSubString.length;i++) {
                            newLine += list[i] + "~";
                        }
                        newLine += "}" + line.substring(last + 1);
                        line = newLine;
                        num = 1;
                    } else {
                        break;
                    }
                }
                sb.append(line).append("\n");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter("dataBase.txt"))) {
                pw.print(sb.toString());
            }
            if (num == 0) {
                System.out.println("{ 'status' : 'error', 'message' : 'The identifier was not valid'}");
            } else {
                System.out.println("{'status':'ok','message':'Post deleted successfully'}");
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }


    public int ifPostExists(String postId) { // check if post postId exists
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String lastLine = "";
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            if (!(lastLine.contains("{"))) {
                addExplore();
            }
        } catch (IOException e) {
            System.out.print(e);
        }

        int num = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String[] postList;
            int j = Integer.parseInt(postId.replace("'", "")) - 1;
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    postList = line.split("~");
                    for (int i = 0; i<postList.length; i++) {
                        if (i == j && postList[i].contains("$")) {
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

    public void getPostDetails(String postId) { // show details of post postId
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String explore = "";
            int j = Integer.parseInt(postId.replace("'", "")) - 1;
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    int start = line.indexOf("{") + 1;
                    int end = line.indexOf("}");
                    explore = line.substring(start, end);
                }
            }
            String[] postList = explore.split("~");
            for (int i = 0; i < postList.length; i++) {
                if (i == j) {
                    int start = postList[i].indexOf("^");
                    int end = postList[i].indexOf("(");
                    int start1 = postList[i].indexOf("$");
                    int end1 = postList[i].indexOf("^");
                    int end2 = postList[i].indexOf(")");
                    int start2 = postList[i].indexOf("=");
                    String postText = postList[i].substring(start+1,end);
                    String person = postList[i].substring(0,start1);
                    String postDate = postList[i].substring(start2+1,end1);
                    String likeList = postList[i].substring(start1+1, start2);
                    int numLike = 0;
                    if (likeList.contains("/")) {
                        numLike = likeList.split("/").length;
                    }
                    String commentZone = postList[i].substring(end+1,end2);
                    String[] commentList = commentZone.split("#");
                    System.out.print("{ 'status' : 'ok', 'message' : [{" + "'post_text':" + postText + ",'post_date':'" +
                            postDate + "','username':" + person + ",'number_of_likes':'" + numLike + "','comments': [");

                    for (int k = 0; k < commentList.length; k++) {
                        int start3 = commentList[k].indexOf("*");
                        int start4 = commentList[k].indexOf("%");
                        int end4 = commentList[k].indexOf("&");
                        String commentDate = commentList[k].substring(start4+1, end4);
                        String commentText = commentList[k].substring(start3+1);
                        String commentWriter = commentList[k].substring(0,start4);
                        String commentLikeList = commentList[k].substring(end4+1, start3);
                        int commentNumLike = 0;
                        if (commentLikeList.contains("<")) {
                            commentNumLike = commentLikeList.split("<").length;
                        }
                        if (k == commentList.length-1) {
                            System.out.print("{'comment_id':'" + (k+1) + "','comment_text':" + commentText +
                                    ",'comment_date':'" + commentDate + "','username':" + commentWriter +
                                    ",'number_of_likes':'" + commentNumLike + "'}]");
                        } else if (k == 0 && commentList.length == 1){
                            System.out.print("{'comment_id':'" + (k+1) + "','comment_text':" + commentText +
                                    ",'comment_date':'" + commentDate + "','username':" + commentWriter +
                                    ",'number_of_likes':'" + commentNumLike + "'}]");
                        } else if (k == 0){
                            System.out.print("{'comment_id':'" + (k+1) + "','comment_text':" + commentText +
                                    ",'comment_date':'" + commentDate + "','username':" + commentWriter +
                                    ",'number_of_likes':'" + commentNumLike + "'},");
                        }
                    }
                    System.out.print(" }] }");
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void getFollowingsPostList(String username) { // shows posts of users who username follows
        java.lang.String followingList1 = "";
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            java.lang.String line1;
            while ((line1 = br.readLine()) != null) {
                java.lang.String[] userInfo = line1.split(",");
                if (userInfo[0].equals(username)) {
                    int start = line1.indexOf("<") + 1;
                    int end = line1.indexOf(">");
                    followingList1 = line1.substring(start, end);
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }

        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String followingList = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    int start = line.indexOf("{") + 1;
                    int end = line.indexOf("}");
                    followingList = line.substring(start, end);
                }
            }
            String[] following = followingList.split("~");
            System.out.print("{ 'status' : 'ok', 'message' : ");
            for (int i = following.length-1; i > -1; i--) {
                int start = following[i].indexOf("^");
                int end = following[i].indexOf("(");
                String postText = following[i].substring(start+1,end);
                int start1 = following[i].indexOf("$");
                int end1 = following[i].indexOf("^");
                int start2 = following[i].indexOf("=");
                String person = following[i].substring(0,start1);
                String postDate = following[i].substring(start2+1,end1);
                if (!(followingList1.contains(person))) {
                    break;
                }
                if (i == 0) {
                    System.out.print("{" + "'post_id':'" + (i+1) + "'," + "'post_text':" + postText +
                            ",'post_date':'" + postDate + "','username':" + person + "}]}");
                } else if (i == following.length-1){
                    System.out.print("[{" + "'post_id':'" + following.length + "'," + "'post_text':" +
                            postText + ",'post_date':'" + postDate + "','username':" + person + "},");
                } else {
                    System.out.print("{" + "'post_id':'" + (i+1) + "'," + "'post_text':" + postText +
                            ",'post_date':'" + postDate + "','username':" + person + "},");
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    public void getUserPosts(String username1) { // shows username1 posts
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {
            String line;
            String followingList = "";
            while ((line = br.readLine()) != null) {
                if (line.contains("{")) {
                    int start = line.indexOf("{") + 1;
                    int end = line.indexOf("}");
                    followingList = line.substring(start, end);
                }
            }
            String[] following = followingList.split("~");
            System.out.print("{ 'status' : 'ok', 'message' : ");
            for (int i = following.length-1; i > -1; i--) {
                if (following[i].contains(username1)) {
                    int start = following[i].indexOf("^");
                    int end = following[i].indexOf("(");
                    String postText = following[i].substring(start+1,end);
                    int end1 = following[i].indexOf("^");
                    int start2 = following[i].indexOf("=");
                    String postDate = following[i].substring(start2+1,end1);
                    if (i == 0) {
                        System.out.print("{" + "'post_id':'" + (i+1) + "'," + "'post_text':" + postText +
                                ",'post_date':'" + postDate +"' }]}");
                    } else if (i == following.length-1){
                        System.out.print("[{" + "'post_id':'" + following.length + "'," + "'post_text':" +
                                postText + ",'post_date':'" + postDate + "' },");
                    } else {
                        System.out.print("{" + "'post_id':'" + (i+1) + "'," + "'post_text':" + postText +
                                ",'post_date':'" + postDate + "' },");
                    }
                }
            }
        } catch (IOException e) {
            System.out.print(e);
        }
    }

    @Override
    public void addLike(String username, String id) { // add like for post postId by username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        int j = Integer.parseInt(id.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            if (i == j) {
                                int num = list[i].indexOf("$");
                                int num2 = list[i].indexOf("=");
                                if (list[i].contains("/")) {
                                    String subLine = line.substring(num+1,num2+1);
                                    if (subLine.contains(username) || list[i].contains(username)) {
                                        System.out.println("{'status':'error','message':'The post identifier to like was not valid'}");
                                        return;
                                    }
                                    subLine += username + "/";
                                    String newLine1 = list[i].substring(0,num) + subLine + list[i].substring(num2);
                                    list[i] = newLine1;
                                } else {
                                    String newLine1 = list[i].substring(0,num+1) + username + "/" +
                                            list[i].substring(num + 1);
                                    list[i] = newLine1;
                                }
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
    public void unlike(String username, String id) { // // unlike post postId by username
        try (BufferedReader br = new BufferedReader(new FileReader("dataBase.txt"))) {StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("}")) {
                    int first = line.indexOf("{");
                    int last = line.indexOf("}");
                    String range = line.substring(first + 1, last);
                    if (range.contains("~")) {
                        String[] list = range.split("~");
                        int j = Integer.parseInt(id.replace("'", "")) - 1;
                        for (int i=0;i<list.length;i++) {
                            if (i == j) {
                                int num = list[i].indexOf("$");
                                int num2 = list[i].indexOf("=");
                                if (list[i].contains("/")) {
                                    String subLine = line.substring(num+2,num2+1);
                                    String[] subsub = subLine.split("/");
                                    String newsubsub = "";
                                    if (subsub.length == 1) {
                                        String newLine1 = list[i].substring(0,num+1) + list[i].substring(num2);
                                        list[i] = newLine1;
                                    } else {
                                        for(int l=0;l<subsub.length;l++){
                                            if(!(subsub[l].equals(username))){
                                                newsubsub += subsub[l]+"/";
                                            }
                                        }
                                        String newLine1 = list[i].substring(0,num+1)+ newsubsub + list[i].substring(num2);
                                        list[i] = newLine1;
                                    }
                                } else {
                                    System.out.println("{ 'status' : 'error', 'message' : 'The post identifier to"+
                                            " unlike was not valid'}");
                                    return;
                                }
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

    public void getMostLikedPosts() { // displays most liked posts
        String[] mostLiked = null;
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
            mostLiked = new String[postList.length];
            for (int i = 0; i < postList.length; i++) {
                int postId = i+1;
                int start = postList[i].indexOf("^");
                int end = postList[i].indexOf("(");
                int start1 = postList[i].indexOf("$");
                int end1 = postList[i].indexOf("^");
                int start2 = postList[i].indexOf("=");
                String postText = postList[i].substring(start+1,end);
                String person = postList[i].substring(0,start1);
                String postDate = postList[i].substring(start2+1,end1);
                String likeList = postList[i].substring(start1+1, start2);
                int numLike = 0;
                if (likeList.contains("/")) {
                    numLike = likeList.split("/").length;
                }
                mostLiked[i] = "{'post_id' : '" + postId + "'," + "'post_text' : " + postText + ", 'post_date':'" +
                        postDate + "', 'username' : " + person + ", 'number_of_likes' : '" + numLike + "' }";
            }
        } catch (IOException e) {
            System.out.print(e);
        }
        sortMaxMin(mostLiked);
        System.out.print("{ 'status' : 'ok', 'message' : [");
        for (int i = 0; i<mostLiked.length && i < 5; i++) {
            if (i == mostLiked.length-1) {
                System.out.print(mostLiked[i]);
            } else {
                System.out.print(mostLiked[i] + ",");
            }
        }
        System.out.print(" ]}");
    }

    public int getLikenum(String post) { // extracts number of likes
        int num = post.indexOf("'number_of_likes' : '");
        num += "'number_of_likes' : '".length();
        int numLike = Integer.parseInt(post.substring(num, num+1));
        return numLike;
    }

    public void sortMaxMin(String[] array) { // sorts array base on like number
        int n = array.length;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                int num1 = getLikenum(array[j]);
                int num2 = getLikenum(array[j + 1]);
                if (num1 < num2) {
                    String temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
    }
}
