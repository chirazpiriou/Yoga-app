describe('404 not found', () => {
  it('Should show the 404 page when the page does not exist', () => {
    cy.visit('/doesntexist');

    cy.get('h1[data-cy=notFound]').should('contain', 'Page not found !');
  });
})