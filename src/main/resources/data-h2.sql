MERGE into TCOURS(CODE,NB_PERIODES,NOM) values 
('IGEB',60,'Gestion de base de données'),
('IPID',60,'Projet de Développement'),
('IVTE',40,'Veille technologique'),
('ISO2',60,'Structure des ordinateurs'),
('IPAP',120,'Principes d''algorithmique et de Programmation'),
('XTAB',80,'Base de données et Tableur'),
('SO2',60,'Architecture des ordinateurs');

delete from TSECTION;
insert  into TSECTION(FKCOURS,SECTION) values ('XTAB','Comptabilité');
insert  into TSECTION(FKCOURS,SECTION) values ('XTAB','Secrétariat');
insert  into TSECTION(FKCOURS,SECTION) values('IPID','Informatique');
insert  into TSECTION(FKCOURS,SECTION) values('IGEB','Informatique');
insert  into TSECTION(FKCOURS,SECTION) values('ISO2','Informatique');
insert  into TSECTION(FKCOURS,SECTION) values('IVTE','Informatique');
insert  into TSECTION(FKCOURS,SECTION) values('IPAP','Informatique');
insert  into TSECTION(FKCOURS,SECTION) values('ISO2','Informatique');


 MERGE INTO TUSER VALUES
('admin','$2a$10$BR98DmjHecYVdqtNz3UGcuyFop/ed0KfxOEAp1bdVXh8eF2iAfOz.','0' ),
('vo', '$2a$10$WvAj8iSJyIxDQWHysC4o6usLkzKW5bi9GThqYH2X/w/vVAm18nWcq','1'),
('wa','$2a$10$ZZ4U5deuZBQqp/BNmKrG1e6OqiRwmaAfu.aaQfgBI9ehImH8AYZKC','1'),
('sh','$2a$10$PdX74d5AzJN/OVU3bP1XbO3fLSfr6x3oKC81rbiUAlSEE6DcID986',1),
('et1','$2a$10$N8pzEKs1350SPT1vpomUo.zMEV1IR/LjXgOrBJ.QaeddZtKLVxR4q',2),
('et2','$2a$10$.Tlzs3a.2PEGK1gckhgaJuz4SWW2J1lWBf.4E3rOleziGDpExtp1W',2),
('et3','$2a$10$eeilUxVqPB8dXMsnzxiIp.OlOyD1isPtICWaRRL5VFofqcWcSURk2',2),
('et4','$2a$10$35yXfuSOyZkLLXFKXQbDLuzvdepJA2cFwxqu.VOsDuuAgf5ujVOuu',2);

MERGE INTO TPROFESSEUR(ID,NOM,PRENOM,EMAIL,FKUSER) VALUES
(1,'Van Oudenhove','Didier','vo@isfce.be','vo'),
(2,'Wafflard','Alain','waff@isfce.be','wa'),
(3,'Huguier','Stephane','sh@isfce.be','sh');
--Réinitialise le compteur identity 
alter table TPROFESSEUR ALTER COLUMN ID RESTART WITH (select max(id)+1 from tprofesseur);


MERGE INTO TMODULE(CODE,DATE_DEBUT,DATE_FIN,MOMENT,FKCOURS,FKPROFESSEUR) values 
('IGEB-1-A','2023-02-03','2023-06-30','1','IGEB','1'),
('IGEB-2-A','2023-02-03','2023-06-30','2','IGEB','1'),
('IVTE-1-A','2022-11-23','2023-01-28','2','IVTE','1'),
('IPID-1-A','2022-09-10','2023-01-28','2','IPID','1'),
('IPAP-1-A','2023-01-31','2023-06-26','1','IPAP','1'),
('IPAP-2-A','2022-01-31','2023-06-26','2','IPAP','1'),
('ISO2-1-A','2022-09-10','2023-01-28','1','ISO2','3'),
('ISO2-2-A','2022-09-10','2023-01-28','2','ISO2','3');

MERGE INTO TETUDIANT (ID ,NOM ,PRENOM ,EMAIL , FKUSER , TEL) VALUES
(1, 'Nom_ET1', 'Prenom_ET1', 'et1@isfce.be', 'et1', ''),
(2, 'Nom_ET2', 'Prenom_ET2', 'et2@isfce.be', 'et2', ''),
(3, 'Nom_ET3', 'Prenom_ET3', 'et3@isfce.be', 'et3', ''),
(4, 'Nom_ET4', 'Prenom_ET4', 'et4@isfce.be', 'et4', '');

MERGE INTO TINSCRIPTION (ID, FKMODULE , FKETUDIANT ) VALUES
(1, 'IPID-1-A' ,1) ,
(2, 'IPID-1-A' ,2) ,
(3, 'IPID-1-A' ,3) ,
(4, 'IVTE-1-A' ,1) ,
(5, 'IVTE-1-A' ,3) ,
(6, 'ISO2-1-A' ,3) ,
(7, 'ISO2-1-A' ,4) ,
(8, 'IPAP-1-A' ,3) ,
(9, 'IPAP-2-A' ,4);