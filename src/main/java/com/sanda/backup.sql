-- we don't know how to generate database onco_cure_assist (class Database) :(
create table if not exists patient
(
	id bigserial not null
		constraint patient_pkey
			primary key,
	full_name text not null,
	gender integer not null,
	birth_date date not null,
	contact_date date not null,
	alive boolean not null,
	survival_month double precision not null,
	employed boolean,
	profession_code integer,
	address varchar(512),
	phone varchar(128),
	patient_card_number varchar(64) not null
)
;

create table if not exists diagnostics
(
	id bigserial not null
		constraint diagnostics_pkey
			primary key,
	patient_id bigint not null
		constraint patient_fk
			references patient,
	"TNM" varchar(16) not null,
	primary_tumour bit(8) not null,
	regional_lymph_nodes bit(8) not null,
	distant_metastasis bit(8) not null,
	tumour_size integer not null,
	side bit(8) not null,
	bronchial_carcinoma boolean,
	lung_carcinoma boolean,
	peribroncial_carcinoma boolean,
	interlobar boolean,
	subcarinal boolean,
	lower_paratracheal boolean,
	upper_paratracheal boolean,
	same_lung_metastasis boolean,
	symptoms boolean,
	"VLC" double precision,
	"TVC" double precision,
	"TIFF_number" double precision,
	volume_force_exp double precision,
	heart_rate integer,
	heart_block boolean,
	extrasistols boolean,
	arythmya boolean,
	somatic boolean,
	"BAC" boolean,
	onco_anamesys boolean,
	stage varchar(16),
	diabetes boolean,
	complains boolean default false,
	grade integer,
	histo_benign_tumor integer[],
	histo_non_lung_tumor integer[],
	histology_diagnosis integer,
	smoking boolean,
	histology_code varchar(64),
	copd boolean,
	tuberculomas boolean,
	chd boolean,
	lcd boolean,
	infl_processes_digestive_tract boolean,
	hepatitis boolean,
	cirrhosis boolean,
	pancreatitis boolean,
	musculoskeletal_diseases boolean,
	infl_diseases_kidneys_bladder boolean,
	prostate_benign_diseases boolean,
	veins_diseases boolean,
	blood_vessels_diseases boolean,
	heart_rhythm_disturbances boolean,
	benign_thyroid_diseases boolean,
	nervous_system_diseases boolean,
	strokes boolean,
	rheumatic_diseases boolean,
	histology_benign_processes boolean,
	anemia boolean
)
;

create table if not exists treatment
(
	id bigserial not null
		constraint treatment_pkey
			primary key,
	surgery_applied boolean not null,
	chemotherapy_applied boolean not null,
	radiation_therapy_applied boolean,
	surgery_code integer,
	surgeon_name varchar,
	patient_id bigint not null
		constraint patient_fk
			references patient,
	surgery_date date,
	first_line_course integer,
	second_line_course integer,
	third_line_course integer
)
;

create table if not exists genetic_predictors
(
	id bigserial not null
		constraint genetic_predictor_pkey
			primary key,
	patient_id bigint not null
		constraint patient_fkey
			references patient,
	vegf_634 varchar(64),
	vegf_2578 varchar(64),
	vegf_936 varchar(64),
	egf varchar(64),
	gstt varchar(64),
	gstm varchar(64),
	gstp varchar(64),
	natkpn varchar(64),
	nattag varchar(64),
	natbam varchar(64),
	acetylation_type varchar(64),
	cyp_1a2 varchar(64),
	cyp_2d6 varchar(64),
	mdr varchar(64),
	egfr_18_tumor varchar(64),
	egfr_18_norm varchar(64),
	egfr_19_tumor varchar(64),
	egfr_19_norm varchar(64),
	egfr_20_tumor varchar(64),
	egfr_20_norm varchar(64),
	egfr_21_tumor varchar(64),
	egfr_21_norm varchar(64),
	egfr_21_blood varchar(64),
	tgf_509 varchar(64),
	tgf_25_codon varchar(64),
	tgfr_206 varchar(64),
	kdr_1719 varchar(64),
	kdr_906 varchar(64),
	sult1 varchar(64),
	mmp9_2660 varchar(64),
	mmp9_1562 varchar(64),
	mmp2_735 varchar(64),
	"ММp2_1575" varchar(64),
	kras_2ex_tumor varchar(64),
	kras_2ex_norm varchar(64),
	pik3ca_9ex varchar(64),
	pik3ca_20ex varchar(64),
	pten varchar(64),
	dnmt_149 varchar(64),
	dnmt_579 varchar(64)
)
;

