export interface IPatientMedicalRecord {
  id?: string;
  patientCondition?: string | null;
  insuranceClaimed?: boolean | null;
}

export class PatientMedicalRecord implements IPatientMedicalRecord {
  constructor(public id?: string, public patientCondition?: string | null, public insuranceClaimed?: boolean | null) {
    this.insuranceClaimed = this.insuranceClaimed ?? false;
  }
}

export function getPatientMedicalRecordIdentifier(patientMedicalRecord: IPatientMedicalRecord): string | undefined {
  return patientMedicalRecord.id;
}
