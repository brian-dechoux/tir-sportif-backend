INSERT INTO price (type,forLicenseeOnly,value,clubId,active) VALUES ('LICENSE',false,10,(SELECT id FROM club),true);
INSERT INTO price (type,forLicenseeOnly,value,clubId,active) VALUES ('CHALLENGE',true,5,(SELECT id FROM club),true);
INSERT INTO price (type,forLicenseeOnly,value,clubId,active) VALUES ('CHALLENGE',false,10,(SELECT id FROM club),true);
