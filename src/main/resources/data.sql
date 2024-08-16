INSERT INTO subject (name, notes_size)
SELECT 'Matematicas', 3
WHERE NOT EXISTS (SELECT 1 FROM subject WHERE name = 'Matematicas');

INSERT INTO subject (name, notes_size)
SELECT 'Ingles', 3
WHERE NOT EXISTS (SELECT 1 FROM subject WHERE name = 'Ingles');

INSERT INTO subject (name, notes_size)
SELECT 'Sociales', 3
WHERE NOT EXISTS (SELECT 1 FROM subject WHERE name = 'Sociales');

INSERT INTO subject (name, notes_size)
SELECT 'Religion', 3
WHERE NOT EXISTS (SELECT 1 FROM subject WHERE name = 'Religion');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Sebastian', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3124556068'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3124556068');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Andres', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556068'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556068');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Alexandra', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556069'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556069');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Sara', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556067'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556067');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Valentina', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556066'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556066');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Mileidy', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556065'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556065');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Genesis', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556064'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556064');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Nahomi', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556063'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556063');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Isabella', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556062'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556062');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Allinson', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556061'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556061');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Alexander', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123556060'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123556060');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Johana', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555059'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555059');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Francisca', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555058'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555058');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Luis', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555057'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555057');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Francisco', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555056'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555056');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Mirian', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555055'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555055');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Celina', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555054'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555054');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Rodolfo', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555053'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555053');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Cesar', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555052'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555052');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Julio', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555051'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555051');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Saida', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555050'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555050');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Aleida', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555049'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555049');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Marcos', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555048'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555048');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Alveiro', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555047'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555047');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Milton', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555046'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555046');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Jairo', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555045'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555045');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Kelly', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555044'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555044');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Michell', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555043'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555043');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Karen', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555042'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555042');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Sandra', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555041'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555041');

INSERT INTO student (first_name, second_name, phone, city, address, surname, second_surname, document_number)
SELECT 'Denise', 'Lor', '312455', 'bogota', 'bella vista', 'lopez', 'lopez', '3123555040'
WHERE NOT EXISTS (SELECT 1 FROM student WHERE document_number = '3123555040');