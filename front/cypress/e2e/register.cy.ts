describe('Register spec', () => {

  it('Should register successfully with correct and valid register fields ', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {statusCode: 200})

    cy.get('input[formControlName=firstName]').type("aline")
    cy.get('input[formControlName=lastName]').type("dupuis")
    cy.get('input[formControlName=email]').type("aline@gmail.com")
    cy.get('input[formControlName=password]').type(`${"chats1254"}{enter}{enter}`)
    cy.url().should('include', '/login')

  })

  it('should disabled submit button if empty email', () => {
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').type("aline")
    cy.get('input[formControlName=lastName]').type("dupuis")
    cy.get('input[formControlName=email]').clear
    cy.get('input[formControlName=password]').type(`${"chats1254"}{enter}{enter}`)
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
    cy.get('button[type=submit]').should('be.disabled')

  })

  it('should disabled submit button if the mail field is filled but another is empty  ', () => {
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').type("aline")
    cy.get('input[formControlName=lastName]').clear;
    cy.get('input[formControlName=email]').type("aline@gmail.com")
    cy.get('input[formControlName=password]').type(`${"chats1254"}{enter}{enter}`)
    cy.get('input[formControlName=lastName]').should('have.class', 'ng-invalid')
    cy.get('button[type=submit]').should('be.disabled')

  }) 

  it('should return error if email already used', () => {
    cy.visit('/register')
    cy.intercept('POST', '/api/auth/register', {statusCode: 400})

    cy.get('input[formControlName=firstName]').type("aline")
    cy.get('input[formControlName=lastName]').type("dupuis")
    cy.get('input[formControlName=email]').type("aline@gmail.com")
    cy.get('input[formControlName=password]').type(`${"chats1254"}{enter}{enter}`)
    cy.get('span[data-cy=redError]').should('be.visible');
  })
})