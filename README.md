MAHMOUDI Mohammadmehdi 322CB Homework : 1

App.java :
in this class I wrote the eraseFile method that deletes everything in the file
dataBase.txt. I also wrote some instructions that are based on the length of the vector
strings and after each check if the operation is of the desired type and sounds
the method with the elements it needs and displays the result. if he doesn't get anything
displays "Hello world!".

User.java :
in this class I wrote two variables username and password and after a constructor which
receives two values ​​when we make an object of the type this class and a constructor that
it only receives username. I wrote checkIfUserExist() which checks if the user exists
or not and returns an int type number. addUsername() which adds the object in
the .txt file in username,password<>[] format. addFollowing() which updates user1
and add the format string "user2!" between "<>". addFollowers() which updates user2
and add the format string "user1@" between "[]". checkIfAlreadyFollowing() checks if
user1 is already a follower of user2. removeFollowing() which unfollows user2 for user1.
removeFollowers() which removes user1 from user2's followers. getFollowingList() which
set a list of followings username. getFollowersList() which displays a list of
followers username. getMostFollowed() which displays a list of 5 users who have the
more followers.

Post.java :
in this class I wrote a post variable and after a constructor that receives
value when we make an object of the type this class and a constructor that
get nothing. addExplore() which adds {} to the last line of the .txt file.
addPost() which adds a post to the last line between "{}" with format:
username+"$"+"="+data+"^"+this.post+"()~". deletePost() which deletes post postId
who wrote username. ifPostExists() checks if post postId exists.
getPostDetails() which displays all postId post details. getFollowingsPostList() which is
as "Home" in Instagram and displays posts by the following username. getUserPosts() which
display username posts. addLike() which likes the post Id in the format:
"username/" between "$=". unlike() which gives dislike to post Id and deletes username.
getMostLikedPosts() which displays 5 posts with the most likes.
and two methods, one for sorting the mostLiked vector and another for extracting the number of likes.

Comment.java :
in this class I wrote a variable comment and after a constructor that receives
value when we make an object of the type this class and a constructor that
get nothing. addComment() which writes a comment for the post postId in the format:
username + "%" + currentDateAsString + "&"+"*" + this.comment+"#" between "()".
checkIfCommentExists() checks if username has left a comment or not.
ifCommentExists() checks if the comment commentId exists. deleteComment() which deletes
comment commnetId who wrote username. addLike() which username likes the comment
id with format: "usernaem<" between "&*". unlike() username gives dislike to comment id.
getMostCommentedPosts() which displays 5 posts with the most comments.
and two methods, one for sorting the mostcommented vector and another for extracting the number of comments.

Likeable.java :
I wrote the addlike and unlike methods that I implemented in the Post and Comment classes.

Cases :
this was the problem like all the posts that exist in
the platform was numbered from 1 to n for postId is more
good that for each user aven postId from 1 to n and one
separate space for randomly displaying all posts from
the platform ie explore.

Proposals:
it would be better if there was an id for each user
in format for example: "@userId".
it would be better if there was an id for each post
based on user id in format for example: "@userId:#postId".
it would be better if there was an id for each comment
based on user id and post id in format for example: "@userId:#postId:$commentId".
