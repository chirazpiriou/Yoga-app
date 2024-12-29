describe('session User spec', () => {

    //1--initially you need to login as a user
    it('should initially login as user', () => {
      cy.visit('/login');
      cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'aline@studio.com',
          firstName: 'firstName',
          lastName: 'lastName',
          admin: false
        },
      });
  
      cy.intercept('GET','/api/session',
        {
            body: [{
              id: 1,
              name: 'Session one',
              description: 'This session is the first one',
              date: '2024-12-29',
              teacher_id: 3,
              users: []
            }]
          }).as('session');
      cy.get('input[data-cy=email]').type("aline@studio.com");
      cy.get('input[data-cy=password]').type(`${"chat1547"}{enter}{enter}`);
      cy.url().should('include', '/sessions');
    })

     //2--The user can participate to the session
     it('should participate to the session ', () => {

        cy.intercept('GET', '/api/teacher/3', {body: [{ id: 3, firstName: 'Alex', lastName: 'LECHAMPS'}]}).as('getTeachers');
        cy.intercept('GET','/api/session/1',
            {
                body: {
                  id: 1,
                  name: 'Session one',
                  description: 'This session is the first one',
                  date: '2024-12-29',
                  teacher_id: 3,
                  users: []
                }
              }).as('sessionOne');

        cy.intercept('POST', '/api/session/1/participate/1', {}).as('participateSession');

        cy.url().should('include', '/sessions');
        cy.get('button[data-cy=detailButton]').click();
        cy.url().should('include', '/sessions/detail')
        cy.get('button[data-cy=participateButton]').should('exist')
        cy.get('button[data-cy=unParticipateButton]').should('not.exist')

        
        //add user number 1 to the session after participation
        cy.intercept('GET', '/api/session/1', {
            body: {
                id: 1,
                name: 'Session one',
                description: 'This session is the first one',
                date: '2024-12-29',
                teacher_id: 3,
                users: [1]
              }
          });

        cy.get('button[data-cy=participateButton]').click()
        cy.wait('@participateSession');
        cy.get('button[data-cy=participateButton]').should('not.exist')
        cy.get('button[data-cy=unParticipateButton]').should('exist') 
        
        
      })

      //2--The user can unParticipate to the session
     it('should unParticipate to the session ', () => {

        cy.intercept('DELETE', '/api/session/1/participate/1', {})
        cy.intercept('GET', '/api/session/1', {
            body: {
                id: 1,
                name: 'Session one',
                description: 'This session is the first one',
                date: '2024-12-29',
                teacher_id: 3,
                users: []
              }
          });

          cy.get('button[data-cy=unParticipateButton]').click()
          cy.get('button[data-cy=participateButton]').should('exist')
          cy.get('button[data-cy=unParticipateButton]').should('not.exist') 
        
        
      })
  
  
    
    
  })