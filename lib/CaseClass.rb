class CaseClass

	def hola
		'hola'
	end

	def initialize
		super
	end

  def self.< (sym)
		puts "Paso por aca.."
		return sym
	end

end