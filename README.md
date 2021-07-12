# muse

## Table of Contents
1. [Overview](#Overview)
1. [Product Spec](#Product-Spec)
1. [Wireframes](#Wireframes)
2. [Schema](#Schema)

## Overview
### Description
Jot down notes, stories, and journal entries from your life, and muse will match that piece of writing with a relevant piece of art from the MET. Build your own unique thought gallery.

### App Evaluation
- **Category:** productivity, health, graphics & design
- **Mobile:** take-on-the-go journaling app
- **Story:** allows users to build a beautiful collection of writings
- **Market:** anyone interested in art, journaling, and a unique intersection of both
- **Habit:** users can write and update their journals daily
- **Scope:** will focus on basic, individual journaling features along with image/text analysis. if time, may expand into shareable galleries

## Product Spec

### 1. User Stories (Required and Optional)

**Required Must-have Stories**

* some analysis of all art in the MET
* some analysis of all text in each journal
* account management (login / logout)
* creating/maintaining a new journal page
* storage of user data (accounts, settings, journal data)
* display "gallery" where you can see / click on all art and its corresponding journal

**Optional Nice-to-have Stories**

* details page about the data analysis
* details page about the artwork
* shareable journals & profiles
* settings page

### 2. Screen Archetypes

* Main Page
   * list of relevant art + corresponding writings
   * can click on piece to edit/write more
   * can add new entry
* Journal Page
   * container of body of writing
   * editable title
   * customization/details button
* Details Page
   * shows more about chosen piece
   * shows more about chosen relevant text
   * shows other close options (lets user select those)
  
### 3. Navigation

**Tab Navigation** (Tab to Screen)

* main
* potential: user profile

**Flow Navigation** (Screen to Screen)

* main -> journal
* main -> potential: user profile / settings
* journal -> details

## Wireframes
<img src="https://i.imgur.com/tMyX34a.jpg" width=600>
<img src="https://i.imgur.com/RtQpdEi.png" width=600>
Figma: https://www.figma.com/file/Vz1DHUDXEyOTtlxly8ueO4/Museum?node-id=0%3A1

## Schema 

### Models

User

| Property    | Type        | Description |
| ----------- | ----------- | ----------  |
| objectId    | String      | unique id for the user (default field)          |
| username    | String      | unique string identifying user                  |
| password    | String      | password for user login                         |
| createdAt   | DateTime    | date when user is created (default field)       |
| updatedAt   | DateTime    | date when user is last updated (default field)  |
| journals    | File        | list of journal objects from user               |
| icon        | File        | image for user icon                             |
| preferences | File        | JSON object about preferences for types of art  |

Journal

| Property    | Type        | Description |
| ----------- | ----------- | ----------  |
| objectId    | String      | unique id for the journal (default field)       |
| createdAt   | DateTime    | date when journal is created (default field)    |
| updatedAt   | DateTime    | date when post is last updated (default field)  |
| title       | String      | title of journal                                |
| contents    | String      | paragraph contents of journal                   |
| cover       | File        | cover art for journal                           |
| author      | Pointer to User | journal author                              |
| art details | File        | JSON object about cover art history & info      |
| text details| File        | JSON object about contents analysis             |


### Networking

Main

| CRUD    | HTTP Verb | Action  |
| ------- | --------- | ------- |
| Create  | POST      | Creating a new journal               |
| Read    | GET       | Query user's journals & covers       |
| Delete  | DELETE    | Deleting an existing journal         |

Journal

| CRUD    | HTTP Verb | Action  |
| ------- | --------- | ------- |
| Update  | PUT       | Update contents of journal           |
| Update  | PUT       | Choose/update cover of journal       |

Profile

| CRUD    | HTTP Verb | Action  |
| ------- | --------- | ------- |
| Read    | GET       | Query logged in user object          |
| Update  | PUT       | Update user profile image.           |

```
// (Read/GET) Query all posts where user is author
ParseQuery<Journal> query = ParseQuery.getQuery(Journal.class);
query.include(Journal.KEY_USER);
query.setLimit(20);
query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
query.addDescendingOrder("createdAt");
query.findInBackground((posts, e) -> {
    if (e != null) {
        Log.e(TAG, "issue fetching posts");
        return;
    }
    adapter.addAll(posts);
});
```
