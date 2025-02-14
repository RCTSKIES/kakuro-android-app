# kakuro-android-app
A Kakuro application for android mobile devices, where one can complete different procedurally generated kakuro levels of varying difficulty (from a 4x4 up to a 30x30).

changes that were done:

changed the MainActivity to PuzzleGameplayActivity -> simply to better organize.

added GuestMainMenuActivity -> Only for guests, it simply displays the different difficulty buttons.

added GuestPuzzleListActivity -> for guests, it will display the puzzles stored in a local file. For that,
we need to figure out how are we going to store the reference of the puzzles in the local file, before they are 
even generated, so that this list activity can display the puzzles buttons with a listview

(later on, we are adding the LoggedPuzzleListActivity for logged users)