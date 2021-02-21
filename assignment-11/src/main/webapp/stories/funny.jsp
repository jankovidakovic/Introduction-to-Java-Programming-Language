<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" session="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Funny story</title>
</head>

<body bgcolor="<% 
	if (request.getSession().getAttribute("pickedBgCol") == null) {
		out.write("white");
	} else {
		out.write(request.getSession().getAttribute("pickedBgCol").toString());
	}
	  %>">
	<p style="color: ${fontColor}">
	When Swami Vivekanand was studying law at the University College, London, 
	a white professor, whose last name was Peters, disliked him intensely. <br/>

	One day, Mr. Peters was having lunch at the dining room when vivekananda came along with his 
	tray and sat next to the professor. <br/>

	The professor said, "Mr Vivekanand , you do not understand. A pig and a bird do not sit together to eat." <br/>

	Vivekanandji looked at him as a parent would a rude child and calmly replied, 
	"You do not worry professor. I'll fly away," and he went and sat at another table. <br/>

	Mr. Peters,  reddened with rage, decided to take revenge.

	The next day in Class he posed the following question: "Mr.Vivekanand , 
	if you were walking down the street and found a package, <br/> and within was a bag of wisdom and another bag with money, 
	which one would you take ?"

	Without hesitating, Vivekanandji responded, "The one with the money, of course." <br/>

	Mr. Peters , smiling sarcastically said, "I, in your place, would have taken the wisdom." <br/>

	Swami Vivekanand shrugged and responded, "Each one takes what he doesn't have." <br/>

	Mr. Peters, by this time was fit to be tied. So great was his anger that 
	he wrote on Swami Vivekanand's exam sheet the word "idiot" and gave it to Swami Vivekanand. <br/>

	Vivekanandji took the exam sheet and sat down at his desk trying very hard to remain calm while he 
	contemplated his next move. <br/>

	A  few  minutes later, Swami Vivekanand got up, went to the professor and told him in a dignified polite tone, 
	"Mr. Peters, you signed the sheet, but you did not give me the grade."
	</p>
	
</body>
</html>