# CPSC 210 Personal Project
## AftFlight *by n9p0w*

This application is a local cross-country flight-planner. <br>


The main use for this application is to automatically generate a flight plan between any two airports
by first having the user input checkpoints, airports, and routes to create a map . <br>

The intended audience for this application are pilots in training for a private pilot's licence
because electronic aids such as ForeFlight and GPS are not allowed for cross-country planning at many flight schools. <br>

This project is of interest to me because it could save student pilots hours of planning using pencil, paper, and calculator,
and thus has a practical use. The alternative of using a spreadsheet is still only good for automating any calculations, and not **route selection** itself.<br>

---
### User Stories
#### Phase 1
- I want to add multiple checkpoints and airports with routes between them to a map
- I want to find the shortest route between two airports
- I want to add and remove en-route points to a flight plan
- I want to calculate and print headings, times, and fuel along the route
#### Phase 2
- I want to save a map, aircraft, and flightplan to file
- I want to load a map, aircraft, and flightplan from a file
#### Phase 3
- I want to do all the above using a gui
- I want my gui to hide buttons when they should not be pressed
---
### Phase 4
#### Task 2
- AircraftTab, FlightPlanTab, and MapTab extends abstract class Tab.
- This was already added as part of phase 3
- They override some abstract methods and the non-abstract method update()
#### Task 3: 
Looking at the diagram, the ui section and model section are mostly separate from each other, which is good since that means less coupling. Also, the gui part is very hierarchical while model looks like a web, and might have a lot of coupling.
<br>

If I had more time, I would remove some semantic coupling for when printing out information about a class in FlightPlanApp and GUI:
- copy paste the code in FlightPlanTab and FlightPlanApp used to turn a FlightPlan's route into a string to a method inside FlightPlan
- copy paste the code in AircraftTab and FlightPlanApp used to turn info of an Aircraft into a string to a method inside Aircraft
- copy paste the code in InspectTool and FlightPlanApp used to show info for a Checkpoint or Airport by turning it into a string to a method inside Checkpoint or Airport
- Airport overrides this method in Checkpoint to also print airport-specific information
- add tests for the new methods since they're inside model now
- make the original spots call the new method instead of generating their own string <br>

Also, I would reduce coupling inside Map:
- change the getAirports() function to iterate through checkpoints and return list of checkpoints which are instanceof Airport
- wherever I use airports, call getAirports() instead
- remove the list of airports
- remove where I add an Airport to airports (only need to add to checkpoints now)
- add tests for getAirports() since its not a simple getter anymore
- remove the association between Map and Airport in the diagram <br>

Also, I would increase cohesion in FlightPlan and Map:
- make a new class called RouteFinder, it will have a bidirectional relationship with FlightPlan
- move the methods used for calculating a route between points into RouteFinder (currently these methods are spread between Map and FlightPlan)
- move the tests from Map to RouteFinder (they were only private helpers inside FlightPlan)
- change the calculateRoute() method in flight plan to use the method inside RouteFinder instead of its private helper functions
- nowhere else are the pathfinding methods inside map used, so we can delete those