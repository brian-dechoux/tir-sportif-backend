INSERT INTO address (street,city,countryId) VALUES ('Rue de Dolhain','Val de Briey',(SELECT id FROM country WHERE name = 'France'));
INSERT INTO club (name,addressId) VALUES ('ST Briey',(SELECT id FROM address));
