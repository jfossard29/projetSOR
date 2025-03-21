INSERT INTO ingredient (nom, description, photo, prix, forme_svg) VALUES
                                                                      ('Tomate', 'Sauce tomate maison', 'tomate.jpg', 1.50, '<circle cx="10" cy="10" r="8" fill="#D32F2F" stroke="#B71C1C" stroke-width="1"/>'),
                                                                      ('Mozzarella', 'Fromage fondant', 'mozzarella.jpg', 2.00, '<circle cx="10" cy="10" r="7" fill="#FFF8E1" stroke="#E0E0E0" stroke-width="1"/>'),
                                                                      ('Jambon', 'Jambon de qualité', 'jambon.jpg', 2.50, '<rect x="5" y="5" width="12" height="8" rx="2" ry="2" fill="#F48FB1" stroke="#D81B60" stroke-width="1"/>'),
                                                                      ('Champignon', 'Champignons frais', 'champignon.jpg', 1.80, '<path d="M 5 10 Q 10 2, 15 10 T 25 10" fill="#D7CCC8" stroke="#A1887F" stroke-width="1"/>'),
                                                                      ('Olive', 'Olives noires', 'olive.jpg', 1.20, '<ellipse cx="10" cy="10" rx="5" ry="7" fill="#2E2E2E" stroke="#1B1B1B" stroke-width="1"/>'),
                                                                      ('Chorizo', 'Chorizo épicé', 'chorizo.jpg', 2.30, '<circle cx="10" cy="10" r="6" fill="#D84315" stroke="#BF360C" stroke-width="1"/>'),
                                                                      ('Poivron', 'Poivrons colorés', 'poivron.jpg', 1.90, '<rect x="5" y="5" width="12" height="12" fill="#4CAF50" stroke="#388E3C" stroke-width="1"/>'),
                                                                      ('Oignon', 'Oignons rouges', 'oignon.jpg', 1.50, '<ellipse cx="10" cy="10" rx="7" ry="5" fill="#AB47BC" stroke="#8E24AA" stroke-width="1"/>'),
                                                                      ('Anchois', 'Anchois salés', 'anchois.jpg', 2.70, '<path d="M 5 10 Q 10 5, 15 10 T 25 10" fill="#546E7A" stroke="#37474F" stroke-width="1"/>'),
                                                                      ('Thon', 'Thon émietté', 'thon.jpg', 2.40, '<path d="M 5 10 L 15 5 L 25 10 L 15 15 Z" fill="#90A4AE" stroke="#607D8B" stroke-width="1"/>'),
                                                                      ('Saumon', 'Saumon fumé', 'saumon.jpg', 3.00, '<rect x="5" y="5" width="10" height="10" fill="#FF7043" stroke="#E64A19" stroke-width="1"/>'),
                                                                      ('Basilic', 'Basilic frais', 'basilic.jpg', 1.00, '<path d="M 10 5 Q 15 10, 10 15 T 5 10 Z" fill="#66BB6A" stroke="#388E3C" stroke-width="1"/>'),
                                                                      ('Parmesan', 'Fromage italien', 'parmesan.jpg', 2.20, '<polygon points="5,10 15,5 25,10 15,15" fill="#FFEB3B" stroke="#FBC02D" stroke-width="1"/>'),
                                                                      ('Boeuf', 'Viande hachée', 'boeuf.jpg', 3.50, '<rect x="5" y="5" width="12" height="10" fill="#A93226" stroke="#7B241C" stroke-width="1"/>'),
                                                                      ('Poulet', 'Poulet mariné', 'poulet.jpg', 3.00, '<circle cx="10" cy="10" r="6" fill="#FFCC80" stroke="#FFA726" stroke-width="1"/>'),
                                                                      ('Œuf', 'Œuf frais', 'oeuf.jpg', 1.80, '<ellipse cx="10" cy="10" rx="6" ry="8" fill="#FFF9C4" stroke="#FDD835" stroke-width="1"/>'),
                                                                      ('Crème', 'Crème fraîche', 'creme.jpg', 2.00, '<circle cx="10" cy="10" r="6" fill="#F5F5F5" stroke="#E0E0E0" stroke-width="1"/>'),
                                                                      ('Ricotta', 'Fromage frais', 'ricotta.jpg', 2.10, '<circle cx="10" cy="10" r="7" fill="#FAFAFA" stroke="#D6D6D6" stroke-width="1"/>'),
                                                                      ('Gorgonzola', 'Fromage bleu', 'gorgonzola.jpg', 2.60, '<circle cx="10" cy="10" r="7" fill="#90CAF9" stroke="#42A5F5" stroke-width="1"/>'),
                                                                      ('Pesto', 'Sauce pesto maison', 'pesto.jpg', 2.30, '<path d="M 5 10 Q 10 5, 15 10 T 25 10" fill="#388E3C" stroke="#2E7D32" stroke-width="1"/>');

-- Insertion des pizzas
INSERT INTO pizza (nom, description, photo, prix) VALUES
                                                      ('Margherita', 'Tomate, mozzarella, basilic', 'margherita.jpg', 8.00),
                                                      ('Reine', 'Tomate, mozzarella, jambon, champignons', 'reine.jpg', 10.50),
                                                      ('4 Fromages', 'Tomate, mozzarella, gorgonzola, parmesan, ricotta', '4fromages.jpg', 11.00),
                                                      ('Napolitaine', 'Tomate, anchois, olives, origan', 'napolitaine.jpg', 9.50),
                                                      ('Calzone', 'Tomate, mozzarella, jambon, œuf', 'calzone.jpg', 10.00),
                                                      ('Chorizo', 'Tomate, mozzarella, chorizo, oignons', 'chorizo.jpg', 11.50),
                                                      ('Végétarienne', 'Tomate, mozzarella, poivrons, champignons, olives', 'vegetarienne.jpg', 10.00),
                                                      ('Savoyarde', 'Crème, reblochon, lardons, oignons', 'savoyarde.jpg', 12.50),
                                                      ('Saumon', 'Crème, saumon fumé, oignons, aneth', 'saumon.jpg', 13.00),
                                                      ('Thon', 'Tomate, mozzarella, thon, oignons', 'thon.jpg', 11.00),
                                                      ('Orientale', 'Tomate, mozzarella, merguez, poivrons', 'orientale.jpg', 12.00),
                                                      ('Hawaïenne', 'Tomate, mozzarella, jambon, ananas', 'hawaiienne.jpg', 10.50),
                                                      ('Carbonara', 'Crème, lardons, oignons, parmesan', 'carbonara.jpg', 11.50),
                                                      ('Bolognaise', 'Tomate, mozzarella, viande hachée, oignons', 'bolognaise.jpg', 12.00),
                                                      ('Pesto', 'Pesto, mozzarella, tomates cerises, ricotta', 'pesto.jpg', 11.00),
                                                      ('4 Saisons', 'Tomate, mozzarella, jambon, champignons, olives, artichauts', '4saisons.jpg', 12.00),
                                                      ('Fromage de Chèvre', 'Tomate, mozzarella, chèvre, miel', 'chevre.jpg', 11.50),
                                                      ('Tartiflette', 'Crème, pommes de terre, reblochon, lardons', 'tartiflette.jpg', 13.50),
                                                      ('Mexicaine', 'Tomate, mozzarella, boeuf épicé, poivrons', 'mexicaine.jpg', 12.50),
                                                      ('Truffe', 'Crème, truffe, champignons, parmesan', 'truffe.jpg', 15.00);

-- Insertion des ingrédients principaux des pizzas
INSERT INTO ingredient_principal (id_ingredient, id_pizza) VALUES
                                                               (1, 1), (2, 1), (12, 1), -- Margherita
                                                               (1, 2), (2, 2), (3, 2), (4, 2), -- Reine
                                                               (1, 3), (2, 3), (13, 3), (18, 3), (19, 3), -- 4 Fromages
                                                               (1, 4), (9, 4), (5, 4), -- Napolitaine
                                                               (1, 5), (2, 5), (3, 5), (16, 5), -- Calzone
                                                               (1, 6), (2, 6), (6, 6), (8, 6), -- Chorizo
                                                               (1, 7), (2, 7), (7, 7), (4, 7), (5, 7), -- Végétarienne
                                                               (17, 8), (14, 8), (8, 8), -- Savoyarde
                                                               (17, 9), (11, 9), (8, 9), -- Saumon
                                                               (1, 10), (2, 10), (10, 10), (8, 10), -- Thon
                                                               (1, 11), (2, 11), (14, 11), (7, 11), -- Orientale
                                                               (1, 12), (2, 12), (3, 12), -- Hawaïenne
                                                               (17, 13), (14, 13), (8, 13), (13, 13), -- Carbonara
                                                               (1, 14), (2, 14), (14, 14), (8, 14), -- Bolognaise
                                                               (20, 15), (2, 15), (18, 15), (12, 15), -- Pesto
                                                               (1, 16), (2, 16), (3, 16), (4, 16), (5, 16), -- 4 Saisons
                                                               (1, 17), (2, 17), (13, 17), -- Fromage de Chèvre
                                                               (17, 18), (14, 18), (8, 18), -- Tartiflette
                                                               (1, 19), (2, 19), (14, 19), (7, 19), -- Mexicaine
                                                               (17, 20), (19, 20), (4, 20), (13, 20); -- Truffe
