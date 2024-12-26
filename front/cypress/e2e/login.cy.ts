describe('Login spec', () => {



  it('should sucessfully let the user Login ', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    }).as('login')  

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    
    //both methods are possible for click or enter enter
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    /*
    cy.get('input[formControlName=password]').type(`${"test!1234"}`)
    cy.get('button[type="submit"]').click();
    */
    
    // Wait for login API response and check status
    cy.wait('@login').then(({ response }) => {       // Wait for the intercepted request named 'login'
      expect(response!.statusCode).to.equal(200);    // Check if HTTP status code is 200 (success)
     });
    
    // Wait for session API response and check status 
    cy.wait('@session').then(({ response }) => {     // Wait for the intercepted request named 'session' 
      expect(response!.statusCode).to.equal(200);    // Check if HTTP status code is 200 (success)
     });

    // Verify redirect to sessions page
    cy.url().should('include', '/sessions')

    
  })

  it('Logout successfull when on the app page ', () => {
    cy.url().should('include', '/sessions');
    cy.get('span[data-cy=logout]').click();
    cy.url().should('not.contain', '/sessions');
    cy.url().should('include', '/')
  });


  it('should return error if credentiels are not valid   ', () => {
    cy.visit('/login')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    
    //both methods are possible for click or enter enter
    cy.get('input[formControlName=password]').type(`'invalid' {enter}{enter}`)


    // Verify redirect to sessions page
    cy.url().should('not.contain', '/sessions')
    cy.get('p[data-cy=redError]').should('be.visible');
 
  })

  it('should disabled submit button if empty email ', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').clear;
    cy.get('input[formControlName=password]').type(`${"wrongpass"}{enter}{enter}`);
    cy.get('p[data-cy=redError]').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disabled submit button if invalid email syntax ', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type("yogastudio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid');
    cy.get('p[data-cy=redError]').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

  it('should disabled submit button if empty password ', () => {
    cy.visit('/login');
    cy.get('input[formControlName=email]').type(`${"yoga@studio.com"}{enter}{enter}`);
    cy.get('input[formControlName=password]').clear;
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid');
    cy.get('p[data-cy=redError]').should('be.visible');
    cy.get('button[type=submit]').should('be.disabled');
  });

  
});