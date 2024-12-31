describe('account-me admin  spec', () => {
    const ADMIN_DETAILS = {
        token: 'jwt',
        type: 'Bearer',
        id: 1,
        email: 'yoga@studio.com',
        firstName: 'Admin',
        lastName: 'ADMIN',
        admin: true,
        createdAt: '2024-01-12T15:33:42',
        updatedAt: '2024-01-12T15:33:42',
      };

    it("should login it is essential to be able to see the conncected user's info ", () => {
        cy.visit('/login')
        cy.intercept('POST', '/api/auth/login', {
        body: {
            token: 'jwt',
            type: 'Bearer',
            id: 1,
            email: 'yoga@studio.com',
            lastName: 'lastName',
            firstName: 'firstName',
            admin: true,
            createdAt: '2024-12-20T00:00:00',
            updatedAt: '2024-12-26T00:00:00',
            
        },
        }).as('login')

        cy.intercept('GET','/api/session',[]).as('session')
        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
        cy.url().should('include', '/sessions')
  })

  it('should show account user admin information', () => {

    cy.intercept('GET', `/api/user/1`, (req) => {
            req.reply(ADMIN_DETAILS);
          });


    cy.get('span[data-cy=goToAccount]').click()
    cy.url().should('include', '/me')
    
    cy.get('p.my2').should('exist');
    cy.get('p.my2').contains('You are admin').should('exist');
    cy.get('button[data-cy=deleteMeButton]').should('not.exist')
  })


})