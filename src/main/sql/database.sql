DROP TABLE FurnitureItems

CREATE TABLE FurnitureItems (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT,
    category TEXT,
    price REAL,
    stockCount INTEGER,
    status TEXT, 
    archived BOOLEAN
);

INSERT INTO FurnitureItems(name, category, price, stockCount, status, archived) VALUES 
('Диван Лофт', 'Диваны', 49990, 3, "IN_STOCK", 0),
('Диван Милан', 'Диваны', 55990, 2, 'IN_STOCK', 0),
('Угловой диван Сканди', 'Диваны', 68990, 1, 'IN_STOCK', 0),
('Стол Орион', 'Столы', 15990, 5, 'IN_STOCK', 0),
('Стол Бруно', 'Столы', 18990, 4, 'IN_STOCK', 0),
('Журнальный столик Рио', 'Столы', 7990, 6, 'IN_STOCK', 0),
('Шкаф Норд', 'Шкафы', 25990, 0, 'OUT_OF_STOCK', 0),
('Шкаф Классик', 'Шкафы', 13990, 0, 'OUT_OF_STOCK', 0),
('Шкаф Купе Лайт', 'Шкафы', 32990, 2, 'IN_STOCK', 0),
('Кровать Астра', 'Кровати', 29990, 2, 'IN_STOCK', 0),
('Кровать Лион', 'Кровати', 34990, 1, 'IN_STOCK', 0),
('Комод Вега', 'Комоды', 11990, 4, 'IN_STOCK', 0),
('Комод Сити', 'Комоды', 9990, 3, 'IN_STOCK', 0),
('Тумба ТВ Нео', 'Тумбы', 8990, 5, 'IN_STOCK', 0),
('Тумба ТВ Ретро', 'Тумбы', 12990, 2, 'IN_STOCK', 0),
('Стул Сканди', 'Стулья', 3990, 10, 'IN_STOCK', 0);