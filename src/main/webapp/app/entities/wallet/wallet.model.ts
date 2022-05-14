export interface IWallet {
  id?: string;
  street?: string | null;
  city?: string | null;
  state?: string | null;
  country?: string | null;
  postCode?: string | null;
  age?: number | null;
  phone?: string | null;
  publicKey?: string | null;
  privateKey?: string | null;
  userId?: string | null;
  login?: string | null;
  lastModifiedBy?: string;
  lastModifiedDate?: Date;
  hospitalName?: string;
}

export class Wallet implements IWallet {
  constructor(
    public id?: string,
    public street?: string | null,
    public city?: string | null,
    public state?: string | null,
    public country?: string | null,
    public postCode?: string | null,
    public age?: number | null,
    public phone?: string | null,
    public publicKey?: string | null,
    public privateKey?: string | null,
    public userId?: string | null,
    public login?: string | null,
    public lastModifiedBy?: string,
    public lastModifiedDate?: Date,
    public hospitalName?: string
  ) {}
}

export function getWalletIdentifier(wallet: IWallet): string | undefined {
  return wallet.id;
}
