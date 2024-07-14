# Tourist-Guide

RAF Tourist Guide is an online platform that allows users to explore and discover destinations worldwide through articles on tourist activities. The platform consists of two main parts: a Content Management System (CMS) for content management and a public platform for reading articles.

# CMS:

    Authentication and Authorization: Implemented authentication using JWT tokens. Only active users have access to the CMS.
    Destination Management: Ability to add, view, modify, and delete destinations. Destinations are used as categories for articles.
    Article Management: Creation, viewing, modification, and deletion of articles. Articles are associated with a specific destination and include information about the author, creation date, number of visits, and comments.
    User Management: Administrators can add, view, modify user types, and activate/deactivate users. Content editors have limited access to the CMS.

# Article Reading Platform:

    Homepage: Displays a list of the 10 latest articles sorted by date. Each article includes a title, brief description, destination, and publication date.
    Most Popular: Lists the top 10 most read articles in the last 30 days.
    Destinations: Shows a list of all destinations. Clicking on a destination displays articles related to that destination.
    Articles: Detailed view of an article including title, text, author, date, tourist activities, and comments. Comments are sorted by creation date. Each article records the number of visits.