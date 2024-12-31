describe('account-me not admin  spec', () => {
  const USER_DETAILS = {
    token: 'jwt',
    type: 'Bearer',
    id: 3,
    email: 'aline@gmail.com',
    lastName: 'aline',
    firstName: 'dupuis',
    admin: false,
    createdAt: '2024-12-20T00:00:00',
    updatedAt: '2024-12-26T00:00:00',
  };

  it("should login it is essential to be able to see the conncected user's info ", () => {
    cy.visit('/login')
    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 3,
        username: 'aline@gmail.com',
        lastName: 'aline',
        firstName: 'dupuis',
        admin: false
      },
    }).as('login')
    cy.intercept('GET','/api/session',[]).as('session')
    cy.get('input[formControlName=email]').type("aline@gmail.com")
    cy.get('input[formControlName=password]').type(`${"chats1547"}{enter}{enter}`)
    cy.url().should('include', '/sessions')
  })

  it('should show account user not admin information', () => {


    cy.intercept('GET', `/api/user/3`, (req) => {
      req.reply(USER_DETAILS);
    });

    cy.get('span[data-cy=goToAccount]').click()
    cy.url().should('include', '/me')
    cy.get('button[data-cy=deleteMeButton]').should('exist')
  })

  it('should delete account when click on delete and and redirect to the session page ', () => {

    cy.intercept('GET', '/api/user/3', {
      body: [{
              id: 3,
              email: 'aline@gmail.com',
              lastName: 'aline',
              firstName: 'dupuis',
              admin: false,
              createdAt: '2024-12-20T00:00:00',
              updatedAt: '2024-12-26T00:00:00',
      }]
    }).as('user1')
    cy.intercept('DELETE', '/api/user/3', {statusCode: 200})
    cy.intercept('GET', '/api/session', {});
    cy.url().should('include', '/me');
    cy.get('button[data-cy=deleteMeButton]').click()
    cy.get('span[data-cy=login]').should('exist')
    cy.get('span[data-cy=register]').should('exist')
  })



})

