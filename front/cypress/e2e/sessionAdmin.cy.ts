describe('session Admin  spec', () => {

  //1--initially you need to login as admin 
  it('should initially login as admin', () => {
    cy.visit('/login');
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept('GET','/api/session',[]).as('session');
    cy.get('input[data-cy=email]').type("yoga@studio.com");
    cy.get('input[data-cy=password]').type(`${"test!1234"}{enter}{enter}`);
    cy.url().should('include', '/sessions');
  })

  //2--Admin can create a new session 
  it('should create a new session', () => {
    cy.url().should('include', '/sessions');

    // Set interception for teachers BEFORE loading page
    cy.intercept('GET', '/api/teacher', {body: [{ id: 2, firstName: 'Aline', lastName: 'DUPUIS'}]}).as('getTeachers');
    // Interception for session creation
    cy.intercept('POST', '/api/session', {
      statusCode: 201,
      body: { id: 1, name: "New Session of yoga", date: "2024-12-29" }
    }).as('createSession');
    //Intercept and simulate the response to the GET request on /api/session after session creation to provide a list of sessions.
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "New Session of yoga",
          date: "2024-12-29",
          description: "This a description of the session",
          teacher: { id: 2, firstName: 'Aline', lastName: 'DUPUIS' }
        }
      ]
    }).as('getSessions');

    cy.get('button[data-cy=buttonCreate]').click();
    cy.get('h1').invoke('text').should('contains', 'Create session')
    cy.get('input[data-cy=name]').type("New Session of yoga");
    cy.get('input[data-cy=date]').type("2024-12-29");
    // Check that the interception has worked before selecting a teacher
    cy.wait('@getTeachers');
    cy.get('mat-select[data-cy=teacherId]').click()
    cy.get('mat-option[data-cy=selectTeacherId]').contains('Aline DUPUIS').click();
    cy.get('textarea[data-cy=description]').type("This a description of the session");
    cy.get('button[data-cy=buttonSubmit]').click();

    //Check that the POST request has been successful
    cy.wait('@createSession');
    cy.wait('@getSessions');
    cy.url().should('include', '/sessions');
    cy.get('button[data-cy=detailButton]').should('be.visible')
    cy.get('button[data-cy=editButton]').should('be.visible')
    
    
  })

  //3--Admin can update the session 
  it('should update the session', () => {
    cy.url().should('include', '/sessions');

    // Set interception for teachers BEFORE loading page
    cy.intercept('GET', '/api/teacher', {body: [{ id: 2, firstName: 'Aline', lastName: 'DUPUIS'}]}).as('getTeachers');
    // Set interception for session to update
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "New Session of yoga",
        date: "2024-12-29",
        description: "This a description of the session",
        teacher: { id: 2, firstName: 'Aline', lastName: 'DUPUIS' },
        users: []
      }
    });
    // Interception for session update
    cy.intercept('PUT', '/api/session/1', {
      statusCode: 201,
      body: { id: 1, name: "New Session updated of yoga", date: "2024-12-30", description: 'This an updated description of the session', }
    }).as('updateSession');
    //Intercept and simulate the response to the GET request on /api/session after session update to provide a list of sessions.
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: [
        {
          id: 1,
          name: "New Session updated of yoga",
          date: "2024-12-30",
          description: "This an updated description of the session",
          teacher: { id: 2, firstName: 'Aline', lastName: 'DUPUIS' }
        }
      ]
    }).as('getSessions');

    cy.get('button[data-cy=editButton]').click();
    cy.url().should('include', '/sessions/update');
    cy.get('h1').invoke('text').should('contains', 'Update session')
    cy.get('input[data-cy=name]').clear().type("New Session updated of yoga");
    cy.get('input[data-cy=date]').clear().type("2024-12-30");
    // Check that the interception has worked before selecting a teacher
    cy.wait('@getTeachers');
    cy.get('mat-select[data-cy=teacherId]').click()
    cy.get('mat-option[data-cy=selectTeacherId]').contains('Aline DUPUIS').click();
    cy.get('textarea[data-cy=description]').clear().type("This an updated description of the session");
    cy.get('button[data-cy=buttonSubmit]').click();

    //Check that the PUT request has been successful
    cy.wait('@updateSession');
    cy.wait('@getSessions');
    cy.url().should('include', '/sessions');
    cy.get('button[data-cy=detailButton]').should('be.visible')
    cy.get('button[data-cy=editButton]').should('be.visible')
    
    
  })

  //4--Admin can delete the session 
  it('should delete the session', () => {
    cy.url().should('include', '/sessions');

    // Set interception for teachers BEFORE loading page
    cy.intercept('GET', '/api/teacher', {body: [{ id: 2, firstName: 'Aline', lastName: 'DUPUIS'}]}).as('getTeachers');
    // Set interception for session to update
    cy.intercept('GET', '/api/session/1', {
      body: {
        id: 1,
        name: "New Session of yoga",
        date: "2024-12-29",
        description: "This a description of the session",
        teacher: { id: 2, firstName: 'Aline', lastName: 'DUPUIS' },
        users: []
      }
    });
    // Interception for session delete
    cy.intercept('DELETE', '/api/session/1', {statusCode: 200}).as('deleteSession');
    //Intercept and simulate the response to the GET request on /api/session after session deleteto provide a list of sessions.
    cy.intercept('GET', '/api/session', {
      statusCode: 200,
      body: []
    }).as('getSessions');

    cy.get('button[data-cy=detailButton]').click();
    cy.url().should('include', '/sessions/detail');
    cy.get('h1').invoke('text').should('contains', 'New Session Of Yoga')
    
    cy.get('button[data-cy=deleteButton]').should('be.visible')
    cy.get('button[data-cy=deleteButton]').click();
    cy.wait('@deleteSession');
    cy.wait('@getSessions');
    cy.url().should('include', '/sessions'); 
  })

  
})