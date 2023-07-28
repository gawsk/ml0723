All of the main code is in the src folder

Running checkout is done through CheckoutServiceImpl.checkout()
This function takes in String toolCode, int rentalDayCount, int discount, LocalDate checkoutDate

toolCode will grab the full Tool from the ToolRepository
rentalDayCount, discount, and checkoutDate are used for the function
checkoutDate uses LocalDate to facilitate different transformations of the data.  Could take in as a String instead and transform the data, but preferred having it already be in the Object I wanted.

The repositories are setup with given data, some tests mock in different data at times.

In a real world scenario, this can be fleshed out more with adding in a checkoutService that links to controllers, having repositories link out to actual DBs, etc.

Used a lib folder for jars instead of using a maven or gradle since this is meant to be a hyper simple project.  Could switch to those for dependency management and build tools.
